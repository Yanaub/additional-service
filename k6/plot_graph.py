import json, sys, os
import pandas as pd
import matplotlib.pyplot as plt

TARGET_LEVELS = [10, 20, 40, 80]

def load_k6_data(filepath):
    rows = []
    with open(filepath, encoding='utf-8') as f:
        for i, line in enumerate(f, 1):
            try:
                d = json.loads(line)
                if d.get('type') == 'Point':
                    data, tags = d.get('data', {}), d.get('data', {}).get('tags', {})
                    rows.append({'time': data.get('time'), 'metric': d.get('metric'),
                                 'value': data.get('value'), 'scenario': tags.get('scenario'),
                                 'expected_response': tags.get('expected_response')})
            except json.JSONDecodeError as e:
                print(f"Ошибка строки {i}: {e}")
    return pd.DataFrame(rows)

def prepare_data(df):
    df['time'] = pd.to_datetime(df['time'])
    df.sort_values('time', inplace=True)
    df['elapsed'] = (df['time'] - df['time'].min()).dt.total_seconds()

    vus = df[df['metric'] == 'vus'][['elapsed', 'value']].rename(columns={'value': 'vus'})
    lat = df[(df['metric'] == 'http_req_duration') & (df['expected_response'] == 'true') & df['value'].notna()][['elapsed', 'value', 'scenario']]
    return pd.merge_asof(lat, vus, on='elapsed', direction='backward').dropna(subset=['vus'])

def aggregate_and_plot(lat_df, out='results/scalability_graph.png'):
    plt.figure(figsize=(10, 6))
    scenarios = [('create_visitors', '#2E86AB', 'POST /visitors/ (create)', 'o'),
                 ('read_rating', '#A23B72', 'GET /exhibits/rating (read)', 's')]
    summary = []

    for sc, col, lbl, mrk in scenarios:
        sub = lat_df[lat_df['scenario'] == sc]
        if sub.empty: continue

        agg = sub.groupby(sub['vus'].apply(lambda v: min(TARGET_LEVELS, key=lambda t: abs(t - v))))['value'] \
            .agg(['mean','count','min','max']).round(2).reset_index()
        agg.columns = ['vus', 'avg', 'count', 'min', 'max']

        print(f"\n{lbl}:\n{'VUs':<6} {'Avg':<8} {'Min':<8} {'Max':<8} {'Count':<6}\n{'-'*40}")
        for r in agg.itertuples():
            print(f"{int(r.vus):<6} {r.avg:<8} {r.min:<8} {r.max:<8} {int(r.count):<6}")
            summary.append({'scenario': sc, 'vus': int(r.vus), **r._asdict()})

        plt.plot(agg['vus'], agg['avg'], marker=mrk, lw=2, color=col, label=lbl)
        for _, r in agg.iterrows():
            plt.annotate(f"{r['avg']:.1f}", (r['vus'], r['avg']),
                         textcoords="offset points", xytext=(0,10), ha='center', fontsize=8, color=col)

    plt.title('Зависимость времени отклика от нагрузки', fontsize=14, pad=20)
    plt.xlabel('VUs'); plt.ylabel('Avg Response Time (ms)')
    plt.xticks(TARGET_LEVELS); plt.legend(); plt.grid(ls='--', alpha=0.5)
    plt.tight_layout(); plt.savefig(out, dpi=150); plt.close()

    if summary:
        pd.DataFrame(summary).to_csv(out.replace('.png', '_summary.csv'), index=False)

def main():
    inp = sys.argv[1] if len(sys.argv)>1 else 'results/k6_output.json'
    out = sys.argv[2] if len(sys.argv)>2 else 'results/scalability_graph.png'
    os.makedirs(os.path.dirname(out) or '.', exist_ok=True)

    df = load_k6_data(inp)
    if df.empty: return print("Нет данных")

    lat = prepare_data(df)
    if lat.empty: return print("Нет замеров отклика")

    print(f"Замеров: {len(lat)} | Сценарии: {lat['scenario'].value_counts().to_dict()}")
    aggregate_and_plot(lat, out)

if __name__ == '__main__': main()
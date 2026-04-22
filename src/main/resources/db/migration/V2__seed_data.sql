
INSERT INTO exhibits (identifier, name, epoch, description) VALUES
  ('a1b2c3d4-e5f6-7890-abcd-ef1234567801', 'Мона Лиза', 'Ренессанс', 'Портрет кисти Леонардо да Винчи'),
  ('a1b2c3d4-e5f6-7890-abcd-ef1234567802', 'Венера Милосская', 'Античность', 'Мраморная скульптура древнегреческой богини'),
  ('a1b2c3d4-e5f6-7890-abcd-ef1234567803', 'Ночной дозор', 'Барокко', 'Полотно Рембрандта ван Рейна')
ON CONFLICT (identifier) DO NOTHING;


INSERT INTO visitors (identifier, full_name, age, ticket_type) VALUES
  ('b1c2d3e4-f5a6-7890-abcd-ef1234567801', 'Иванов Иван Иванович', 35, 'FULL'),
  ('b1c2d3e4-f5a6-7890-abcd-ef1234567802', 'Петрова Мария Сергеевна', 17, 'DISCOUNTED'),
  ('b1c2d3e4-f5a6-7890-abcd-ef1234567803', 'Сидоров Алексей Петрович', 65, 'DISCOUNTED')
ON CONFLICT (identifier) DO NOTHING;


INSERT INTO excursions (identifier, date, guide) VALUES
  ('c1d2e3f4-a5b6-7890-abcd-ef1234567801', CURRENT_DATE, 'Козлов Дмитрий'),
  ('c1d2e3f4-a5b6-7890-abcd-ef1234567802', CURRENT_DATE, 'Николаева Анна'),
  ('c1d2e3f4-a5b6-7890-abcd-ef1234567803', CURRENT_DATE, 'Козлов Дмитрий')
ON CONFLICT (identifier) DO NOTHING;


INSERT INTO excursion_exhibits (excursion_id, exhibit_id) VALUES
  ('c1d2e3f4-a5b6-7890-abcd-ef1234567801', 'a1b2c3d4-e5f6-7890-abcd-ef1234567801'),
  ('c1d2e3f4-a5b6-7890-abcd-ef1234567801', 'a1b2c3d4-e5f6-7890-abcd-ef1234567802'),
  ('c1d2e3f4-a5b6-7890-abcd-ef1234567802', 'a1b2c3d4-e5f6-7890-abcd-ef1234567802'),
  ('c1d2e3f4-a5b6-7890-abcd-ef1234567802', 'a1b2c3d4-e5f6-7890-abcd-ef1234567803'),
  ('c1d2e3f4-a5b6-7890-abcd-ef1234567803', 'a1b2c3d4-e5f6-7890-abcd-ef1234567801')
ON CONFLICT (excursion_id, exhibit_id) DO NOTHING;


INSERT INTO excursion_visitors (excursion_id, visitor_id) VALUES
  ('c1d2e3f4-a5b6-7890-abcd-ef1234567801', 'b1c2d3e4-f5a6-7890-abcd-ef1234567801'),
  ('c1d2e3f4-a5b6-7890-abcd-ef1234567801', 'b1c2d3e4-f5a6-7890-abcd-ef1234567802'),
  ('c1d2e3f4-a5b6-7890-abcd-ef1234567802', 'b1c2d3e4-f5a6-7890-abcd-ef1234567803'),
  ('c1d2e3f4-a5b6-7890-abcd-ef1234567803', 'b1c2d3e4-f5a6-7890-abcd-ef1234567801'),
  ('c1d2e3f4-a5b6-7890-abcd-ef1234567803', 'b1c2d3e4-f5a6-7890-abcd-ef1234567802')
ON CONFLICT (excursion_id, visitor_id) DO NOTHING;
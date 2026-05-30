-- ============================================================
-- NGN – Nova Gaia Network  |  Dados iniciais para demonstração
-- ============================================================

-- Habitat principal
INSERT INTO tb_habitat (name, population, available_water, available_energy,
    oxygen_level, temperature, humidity, pressure, radiation_level)
VALUES ('Estação Nova Gaia Alpha', 120, 15000.0, 500.0,
        20.9, 22.0, 45.0, 101.3, 0.1);

INSERT INTO tb_habitat (name, population, available_water, available_energy,
    oxygen_level, temperature, humidity, pressure, radiation_level)
VALUES ('Base Lunar Artemis', 48, 8000.0, 320.0,
        20.5, 18.0, 40.0, 98.0, 0.3);

-- Módulos (Habitat 1)
INSERT INTO tb_module (name, type, active, energy_consumption, habitat_id)
VALUES ('Suporte à Vida Alpha-1', 'LIFE_SUPPORT', true,  80.0, 1);
INSERT INTO tb_module (name, type, active, energy_consumption, habitat_id)
VALUES ('Gerador Solar Alpha',    'ENERGY',       true,  10.0, 1);
INSERT INTO tb_module (name, type, active, energy_consumption, habitat_id)
VALUES ('Defesa Orbital Alpha',   'DEFENSE',      true,  60.0, 1);
INSERT INTO tb_module (name, type, active, energy_consumption, habitat_id)
VALUES ('Cultivo Hidropônico A',  'AGRICULTURE',  true,  35.0, 1);
INSERT INTO tb_module (name, type, active, energy_consumption, habitat_id)
VALUES ('Lab de Pesquisa Alpha',  'RESEARCH',     false, 25.0, 1);

-- Módulos (Habitat 2)
INSERT INTO tb_module (name, type, active, energy_consumption, habitat_id)
VALUES ('Suporte à Vida Artemis', 'LIFE_SUPPORT', true,  60.0, 2);
INSERT INTO tb_module (name, type, active, energy_consumption, habitat_id)
VALUES ('Defesa Lunar Artemis',   'DEFENSE',      true,  55.0, 2);

-- Sensores (Módulo 1 – LIFE_SUPPORT)
INSERT INTO tb_sensor (name, type, current_value, active, latitude, longitude, module_id)
VALUES ('O2-Sensor-A1', 'OXYGEN',      20.9,  true,  -3.7, -38.5, 1);
INSERT INTO tb_sensor (name, type, current_value, active, latitude, longitude, module_id)
VALUES ('Temp-A1',      'TEMPERATURE', 22.0,  true,  -3.7, -38.5, 1);
INSERT INTO tb_sensor (name, type, current_value, active, latitude, longitude, module_id)
VALUES ('Press-A1',     'PRESSURE',    101.3, true,  -3.7, -38.5, 1);
INSERT INTO tb_sensor (name, type, current_value, active, latitude, longitude, module_id)
VALUES ('Hum-A1',       'HUMIDITY',    45.0,  true,  -3.7, -38.5, 1);

-- Sensores (Módulo 3 – DEFENSE)
INSERT INTO tb_sensor (name, type, current_value, active, latitude, longitude, module_id)
VALUES ('Rad-Defense-A1', 'RADIATION', 0.1, true, -3.8, -38.6, 3);

-- Sensores (Módulo 4 – AGRICULTURE)
INSERT INTO tb_sensor (name, type, current_value, active, latitude, longitude, module_id)
VALUES ('Water-Agri-A1', 'WATER',          12000.0, true,  -3.9, -38.7, 4);
INSERT INTO tb_sensor (name, type, current_value, active, latitude, longitude, module_id)
VALUES ('Food-Agri-A1',  'FOOD_PRODUCTION', 45.0,   true,  -3.9, -38.7, 4);

-- Membros da tripulação
INSERT INTO tb_crew_member (name, role, experience_level)
VALUES ('Dra. Lena Martins',  'Médica Chefe',          9);
INSERT INTO tb_crew_member (name, role, experience_level)
VALUES ('Eng. Carlos Lima',   'Engenheiro de Energia',  8);
INSERT INTO tb_crew_member (name, role, experience_level)
VALUES ('Cmte. Ana Souza',    'Comandante',             10);
INSERT INTO tb_crew_member (name, role, experience_level)
VALUES ('Biol. Pedro Costa',  'Agrônomo Espacial',       7);

-- Alocação de tripulação (ManyToMany: tb_module_crew)
INSERT INTO tb_module_crew (module_id, crew_member_id) VALUES (1, 1); -- Lena em LIFE_SUPPORT
INSERT INTO tb_module_crew (module_id, crew_member_id) VALUES (2, 2); -- Carlos em ENERGY
INSERT INTO tb_module_crew (module_id, crew_member_id) VALUES (3, 3); -- Ana em DEFENSE
INSERT INTO tb_module_crew (module_id, crew_member_id) VALUES (4, 4); -- Pedro em AGRICULTURE
INSERT INTO tb_module_crew (module_id, crew_member_id) VALUES (1, 3); -- Ana também em LIFE_SUPPORT

-- Métricas de recursos
INSERT INTO tb_resource_metric
    (oxygen_level, water_level, pressure, temperature, humidity, air_quality, food_production, collected_at, habitat_id)
VALUES (20.9, 14500.0, 101.3, 22.0, 45.0, 35.0, 45.0, CURRENT_TIMESTAMP(), 1);

INSERT INTO tb_resource_metric
    (oxygen_level, water_level, pressure, temperature, humidity, air_quality, food_production, collected_at, habitat_id)
VALUES (20.5, 7800.0, 98.0, 18.0, 40.0, 28.0, 20.0, CURRENT_TIMESTAMP(), 2);

-- Eventos espaciais históricos
INSERT INTO tb_space_event (type, description, danger_level, event_date, latitude, longitude)
VALUES ('SOLAR_FLARE', 'Explosão solar classe M detectada pelo NASA DONKI', 5.5, CURRENT_TIMESTAMP(), 0.0, 0.0);

INSERT INTO tb_space_event (type, description, danger_level, event_date, latitude, longitude)
VALUES ('METEOR', 'Asteroide de 12m detectado em órbita próxima (NASA NeoWs)', 7.2, CURRENT_TIMESTAMP(), 0.0, 0.0);

-- Dataset inicial (equivalente a seed-data.json de fases previas)

INSERT INTO categories (id, label, tagline) VALUES
    ('classic', 'Clásicos', 'Potencia probada para campo y finca'),
    ('autonomous', 'Autónomos', 'Guiado preciso y telemetría integrada');

INSERT INTO products (id, name, category_id) VALUES
    ('agri-classic-100', 'AgriClassic 100', 'classic'),
    ('autonomous-x1', 'AutoDrive X1', 'autonomous'),
    ('compact-50', 'CompactPro 50', 'classic'),
    ('mini-hydro-20', 'MiniHydro 20', 'classic');

INSERT INTO product_highlights (product_id, sort_order, highlight) VALUES
    ('agri-classic-100', 0, 'Motor diésel 95 CV'),
    ('agri-classic-100', 1, 'Transmisión mecánica 12x12'),
    ('agri-classic-100', 2, 'Cabina climatizada opcional'),
    ('autonomous-x1', 0, 'Guiado RTK integrado'),
    ('autonomous-x1', 1, 'Red eléctrica híbrida'),
    ('autonomous-x1', 2, 'Telemetría en tiempo real'),
    ('compact-50', 0, 'Ideal para viñedos y invernaderos'),
    ('compact-50', 1, 'Radio de giro corto'),
    ('compact-50', 2, 'Enganche III categoría'),
    ('mini-hydro-20', 0, 'Hidráulico dedicado para cargador frontal'),
    ('mini-hydro-20', 1, 'Pacto urbano y fincas pequeñas');

INSERT INTO variants (sku, product_id, label, color_hex, price) VALUES
    ('AGR-100-RED', 'agri-classic-100', 'Rojo granja', '#C0392B', 78500.00),
    ('AGR-100-GREEN', 'agri-classic-100', 'Verde campo', '#1E8449', 78500.00),
    ('AGR-100-YELLOW', 'agri-classic-100', 'Amarillo seguridad', '#F1C40F', 79900.00),
    ('AUTO-X1-BLUE', 'autonomous-x1', 'Azul cielo', '#2980B9', 142000.00),
    ('AUTO-X1-WHITE', 'autonomous-x1', 'Blanco polar', '#ECF0F1', 142000.00),
    ('AUTO-X1-GRAPHITE', 'autonomous-x1', 'Grafito', '#566573', 144500.00),
    ('COM-50-BLACK', 'compact-50', 'Negro mate', '#17202A', 51200.00),
    ('COM-50-SILVER', 'compact-50', 'Plata industrial', '#BDC3C7', 51200.00),
    ('COM-50-ORANGE', 'compact-50', 'Naranja alta visibilidad', '#E67E22', 52800.00),
    ('MIN-20-RED', 'mini-hydro-20', 'Rojo cereza', '#922B21', 28900.00),
    ('MIN-20-NAVY', 'mini-hydro-20', 'Azul marino', '#1B4F72', 28900.00),
    ('MIN-20-CAMO', 'mini-hydro-20', 'Verde bosque', '#145A32', 29500.00);

INSERT INTO stores (id, name, city, address_line) VALUES
    ('store-bogota-norte', 'Tractor Store Bogotá Norte', 'Bogotá', 'Av. Carrera 7 #127-48'),
    ('store-medellin', 'Tractor Store Medellín', 'Medellín', 'Calle 10 #43A-30'),
    ('store-cali', 'Tractor Store Cali', 'Cali', 'Calle 5 #38-24');

INSERT INTO inventory (sku, available_units) VALUES
    ('AGR-100-RED', 4),
    ('AGR-100-GREEN', 0),
    ('AGR-100-YELLOW', 12),
    ('AUTO-X1-BLUE', 3),
    ('AUTO-X1-WHITE', 7),
    ('AUTO-X1-GRAPHITE', 2),
    ('COM-50-BLACK', 5),
    ('COM-50-SILVER', 6),
    ('COM-50-ORANGE', 1),
    ('MIN-20-RED', 8),
    ('MIN-20-NAVY', 4),
    ('MIN-20-CAMO', 0);

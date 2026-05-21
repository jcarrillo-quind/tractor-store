-- URLs completas de imágenes (CDN blueprint Tractor Store)

ALTER TABLE categories ADD COLUMN image_url VARCHAR(512) NOT NULL DEFAULT '';

ALTER TABLE variants ADD COLUMN image_url VARCHAR(512) NOT NULL DEFAULT '';

ALTER TABLE stores ADD COLUMN image_url VARCHAR(512) NOT NULL DEFAULT '';

UPDATE categories SET image_url = 'https://blueprint.the-tractor.store/cdn/img/scene/500/classics.webp'
WHERE id = 'classic';

UPDATE categories SET image_url = 'https://blueprint.the-tractor.store/cdn/img/scene/500/autonomous.webp'
WHERE id = 'autonomous';

UPDATE variants SET image_url = 'https://blueprint.the-tractor.store/cdn/img/product/200/CL-10-SD.webp'
WHERE sku = 'AGR-100-RED';

UPDATE variants SET image_url = 'https://blueprint.the-tractor.store/cdn/img/product/200/CL-08-GR.webp'
WHERE sku = 'AGR-100-GREEN';

UPDATE variants SET image_url = 'https://blueprint.the-tractor.store/cdn/img/product/200/CL-05-PT.webp'
WHERE sku = 'AGR-100-YELLOW';

UPDATE variants SET image_url = 'https://blueprint.the-tractor.store/cdn/img/product/200/AU-01-SI.webp'
WHERE sku = 'AUTO-X1-BLUE';

UPDATE variants SET image_url = 'https://blueprint.the-tractor.store/cdn/img/product/200/AU-08-WH.webp'
WHERE sku = 'AUTO-X1-WHITE';

UPDATE variants SET image_url = 'https://blueprint.the-tractor.store/cdn/img/product/200/AU-07-MT.webp'
WHERE sku = 'AUTO-X1-GRAPHITE';

UPDATE variants SET image_url = 'https://blueprint.the-tractor.store/cdn/img/product/200/CL-04-BL.webp'
WHERE sku = 'COM-50-BLACK';

UPDATE variants SET image_url = 'https://blueprint.the-tractor.store/cdn/img/product/200/AU-01-SI.webp'
WHERE sku = 'COM-50-SILVER';

UPDATE variants SET image_url = 'https://blueprint.the-tractor.store/cdn/img/product/200/AU-02-OG.webp'
WHERE sku = 'COM-50-ORANGE';

UPDATE variants SET image_url = 'https://blueprint.the-tractor.store/cdn/img/product/200/CL-10-SD.webp'
WHERE sku = 'MIN-20-RED';

UPDATE variants SET image_url = 'https://blueprint.the-tractor.store/cdn/img/product/200/CL-02-BL.webp'
WHERE sku = 'MIN-20-NAVY';

UPDATE variants SET image_url = 'https://blueprint.the-tractor.store/cdn/img/product/200/CL-03-GR.webp'
WHERE sku = 'MIN-20-CAMO';

UPDATE stores SET image_url = 'https://blueprint.the-tractor.store/cdn/img/store/200/store-1.webp'
WHERE id = 'store-bogota-norte';

UPDATE stores SET image_url = 'https://blueprint.the-tractor.store/cdn/img/store/200/store-2.webp'
WHERE id = 'store-medellin';

UPDATE stores SET image_url = 'https://blueprint.the-tractor.store/cdn/img/store/200/store-3.webp'
WHERE id = 'store-cali';

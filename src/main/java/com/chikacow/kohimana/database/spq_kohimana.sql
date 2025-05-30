DELIMITER //

CREATE PROCEDURE get_products_by_codes(IN codes TEXT)
BEGIN
SELECT * FROM tbl_product
WHERE FIND_IN_SET(code, codes) > 0;
END //

DELIMITER ;


DELIMITER //

CREATE PROCEDURE get_products_by_ids(IN ids TEXT)
BEGIN
SELECT * FROM tbl_product
WHERE FIND_IN_SET(id, ids) > 0;
END //

DELIMITER ;





DELIMITER //

CREATE PROCEDURE get_products_by_codes(IN codes TEXT)
BEGIN
SELECT * FROM product
WHERE FIND_IN_SET(code, codes) > 0;
END //

DELIMITER ;




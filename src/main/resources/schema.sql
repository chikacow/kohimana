INSERT INTO `kohimana`.`tbl_category` ( `name`, `type`, `category_id`)
SELECT 'other', 'FOOD', 'cate_other'
FROM DUAL
WHERE NOT EXISTS (
    SELECT 1 FROM `kohimana`.`tbl_category`
    WHERE `name` = 'other' OR `category_id` = 'cate_other'
);

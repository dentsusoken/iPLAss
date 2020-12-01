DROP TABLE IF EXISTS `cache_store`;
CREATE TABLE `cache_store`
(
    `ns` VARCHAR(256) NOT NULL,
    `c_key` VARCHAR(256) NOT NULL,
    `c_val` LONGBLOB,
    `ver` BIGINT(19),
    `cre_time` BIGINT(19),
    `inv_time` BIGINT(19),
    `ci_0` VARCHAR(256),
    `ci_1` VARCHAR(256),
    `ci_2` VARCHAR(256),
    PRIMARY KEY (`ns`, `c_key`),
    INDEX `cache_store_index0` (`ns`, `ci_0`),
    INDEX `cache_store_index1` (`ns`, `ci_1`),
    INDEX `cache_store_index2` (`ns`, `ci_2`)
)
ENGINE=InnoDB COMPRESSION="zlib"
;

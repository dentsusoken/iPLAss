SET FOREIGN_KEY_CHECKS = 0;

USE `mtdb`;

source obj_ref_rb.sql
source obj_ref.sql
source obj_store_rb.sql
source obj_store.sql

source obj_index_date.sql
source obj_index_dbl.sql
source obj_index_num.sql
source obj_index_str.sql
source obj_index_ts.sql

source obj_unique_date.sql
source obj_unique_dbl.sql
source obj_unique_num.sql
source obj_unique_str.sql
source obj_unique_ts.sql

SET FOREIGN_KEY_CHECKS = 1;

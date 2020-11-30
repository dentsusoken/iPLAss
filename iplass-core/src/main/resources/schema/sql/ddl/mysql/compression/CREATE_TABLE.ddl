SET FOREIGN_KEY_CHECKS = 0;

USE `mtdb`;

source obj_ref_rb.sql
source obj_ref.sql
source obj_store_rb.sql
source obj_store.sql

source cache_store.sql
source crawl_log.sql
source delete_log.sql
source obj_blob.sql
source lob_store.sql
source obj_blob_rb.sql
source obj_index_date.sql
source obj_index_dbl.sql
source obj_index_num.sql
source obj_index_str.sql
source obj_index_ts.sql
source obj_meta.sql
source obj_unique_date.sql
source obj_unique_dbl.sql
source obj_unique_num.sql
source obj_unique_str.sql
source obj_unique_ts.sql
source schema_ctrl.sql
source t_account.sql
source t_atoken.sql
source t_pass_hi.sql
source t_tenant.sql
source counter.sql
source task_queue.sql
source task_queue_hi.sql

SET FOREIGN_KEY_CHECKS = 1;

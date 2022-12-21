create table cell
(
    id           number,
    ibf_id       number,
    cell_index   number,
    row_hash_sum varchar(256),
    key_sums     clob,
    count      number
)
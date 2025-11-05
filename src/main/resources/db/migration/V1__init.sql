-- todo: set varchar sizes and address should be jsonb, also add mtime, ctime

create table if not exists recipients (
    id uuid primary key,
    name varchar not null,
    address varchar not null
);


create table if not exists abwicklungen (
    id uuid primary key,
    licence_plate varchar,
    payment_state varchar,
    recipient_id uuid references recipients(id),
    print_id varchar
);
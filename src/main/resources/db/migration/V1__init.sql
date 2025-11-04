-- todo: set varchar sizes and address should be jsonb

create table if not exists recipients (
    id varchar primary key,
    name varchar not null,
    address varchar not null
);


create table if not exists abwicklungen (
    id varchar not null,
    licence_plate varchar not null,
    payment_state varchar not null,
    recipient_id varchar references recipients(id),
    print_id varchar,
    primary key (id)
);
create table if not exists "orders" (
    id               uuid primary key default gen_random_uuid(),
    status           varchar(100) not null,
    total_price      numeric(10,2) not null,
    client_id        uuid         not null,
    created_at       timestamp    not null,
    updated_at       timestamp,
    created_by       varchar(100) not null,
    updated_by       varchar(100)
);

create table if not exists product
(
    id          uuid primary key default gen_random_uuid(),
    name        varchar(100) not null,
    description varchar(1000),
    price       numeric(10,2) not null,
    quantity    numeric(10,2) not null,
    tag         varchar(100) not null,
    active      boolean      not null,
    created_at  timestamp    not null,
    updated_at  timestamp,
    created_by  varchar(100) not null,
    updated_by  varchar(100)
);

create table if not exists "order_item"
(
    id         uuid primary key default gen_random_uuid(),
    "orders_id"   uuid         not null,
    product_id uuid         not null,
    quantity   numeric(10,2) not null,
    relative_price numeric(10,2) not null,
    created_at timestamp not null,
    updated_at timestamp,
    created_by varchar(100) not null,
    updated_by varchar(100),
    constraint fk_orders foreign key ("orders_id") references "orders" ("id"),
    constraint fk_product foreign key (product_id) references product (id)
);

create table if not exists client
(
    id          uuid primary key default gen_random_uuid(),
    name        varchar(100) not null,
    email       varchar(100) not null,
    created_at  timestamp    not null,
    updated_at  timestamp,
    created_by  varchar(100) not null,
    updated_by  varchar(100)
);
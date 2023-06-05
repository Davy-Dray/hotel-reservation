create table app_user
(
    id           BIGSERIAL PRIMARY KEY,
    email        text NOT NULL,
    firstname    text NOT NULL,
    lastname     text NOT NULL,
    password     text NOT NULL,
    phone_number text NOT NULL
);

create table reservation
(
    id               BIGSERIAL PRIMARY KEY,
    check_in_date    date      not null,
    check_out_date   date      not null,
    created_at       date      not null,
    has_checked_in   boolean   not null,
    status           text NOT NULL,
    total_charge     float(53) not null,
    transaction_id   text NOT NULL,
    reserved_room_id bigserial,
    user_id          bigserial,
    reservation_id   bigserial

);

create table reservation_history
(
    id               BIGSERIAL PRIMARY KEY,
    action_performed varchar(255),
    date_time        timestamp(6),
    reservation_id   bigserial
);

create table room
(
    id            BIGSERIAL PRIMARY KEY,
    booking_price float(53) not null,
    room_number   integer   not null,
    room_status   text NOT NULL,
    room_type     text NOT NULL

);
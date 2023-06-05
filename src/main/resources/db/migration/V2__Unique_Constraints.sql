ALTER TABLE app_user
    ADD CONSTRAINT app_user_email_unique UNIQUE (email);


ALTER TABLE reservation
    ADD CONSTRAINT transaction_id_unique UNIQUE (transaction_id);

ALTER TABLE room
    ADD CONSTRAINT room_number_unique UNIQUE (room_number);


ALTER TABLE reservation
    ADD CONSTRAINT fk_user_id
        FOREIGN KEY (user_id)
            REFERENCES app_user (id);

ALTER TABLE reservation
    ADD CONSTRAINT fk_reserved_room_id
        FOREIGN KEY (reserved_room_id)
            REFERENCES room (id);

ALTER TABLE reservation_history
    ADD CONSTRAINT fk_reservation_id
        FOREIGN KEY (reservation_id)
            REFERENCES reservation (id);
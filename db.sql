create table if not exists users (
    id bigserial primary key,
    email text not null unique,
    username text not null unique,
    hashed_password text not null,
    bio text,
    image text
);

create table if not exists tokens (
    id bigserial primary key,
    jwt text not null,
    identity bigint not null references users (id),
    expiry timestamp not null,
    last_touched timestamp
);

create table if not exists follow_associations (
    id bigserial primary key,
    follower_id bigint not null references users (id),
    followee_id bigint not null references users (id),
    constraint cannot_follow_self check (follower_id != followee_id)
)

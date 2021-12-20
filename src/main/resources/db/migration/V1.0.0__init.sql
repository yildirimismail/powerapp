create table permission (id  serial not null, name varchar(255), primary key (id));
create table permission_role (role_id int4 not null, permission_id int4 not null);
create table role (id  serial not null, name varchar(255), primary key (id));
create table role_user (user_id int4 not null, role_id int4 not null);
create table users (id  serial not null, account_expired boolean, account_locked boolean, credentials_expired boolean, email varchar(255), enabled boolean not null, password varchar(255), username varchar(255), primary key (id));
alter table permission_role add constraint FK3tuvkbyi6wcytyg21hvpd6txw foreign key (permission_id) references permission;
alter table permission_role add constraint FK50sfdcvbvdaclpn7wp4uop4ml foreign key (role_id) references role;
alter table role_user add constraint FKiqpmjd2qb4rdkej916ymonic6 foreign key (role_id) references role;
alter table role_user add constraint FK4320p8bgvumlxjkohtbj214qi foreign key (user_id) references users;

create table oauth_client_details
(
    client_id               varchar not null
        constraint oauth_client_details_pk
            primary key,
    client_secret           varchar,
    resource_ids            varchar,
    scope                   varchar,
    authorized_grant_types  varchar,
    web_server_redirect_uri varchar,
    authorities             varchar,
    access_token_validity   integer,
    refresh_token_validity  integer,
    additional_information  varchar,
    autoapprove             varchar
);



INSERT INTO public.oauth_client_details (client_id, client_secret, resource_ids, scope, authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, refresh_token_validity, additional_information, autoapprove) VALUES ('USER_CLIENT_APP', '{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi', 'USER_CLIENT_RESOURCE,USER_ADMIN_RESOURCE', 'role_admin,role_user', 'authorization_code,password,refresh_token,implicit', null, null, 900, 3600, '{}', null);
INSERT INTO public.users (id, account_expired, account_locked, credentials_expired, email, enabled, password, username) VALUES (1, false, false, false, 'william@gmail.com', true, '{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi', 'admin');
INSERT INTO public.users (id, account_expired, account_locked, credentials_expired, email, enabled, password, username) VALUES (2, false, false, false, 'john@gmail.com', true, '{bcrypt}$2a$10$EOs8VROb14e7ZnydvXECA.4LoIhPOoFHKvVF/iBZ/ker17Eocz4Vi', 'user');

INSERT INTO public.permission (id, name) VALUES (1, 'can_create_user');
INSERT INTO public.permission (id, name) VALUES (2, 'can_update_user');
INSERT INTO public.permission (id, name) VALUES (3, 'can_read_user');
INSERT INTO public.permission (id, name) VALUES (4, 'can_delete_user');

INSERT INTO public.role (id, name) VALUES (1, 'role_admin');
INSERT INTO public.role (id, name) VALUES (2, 'role_user');

INSERT INTO public.role_user (user_id, role_id) VALUES (1, 1);
INSERT INTO public.role_user (user_id, role_id) VALUES (2, 2);

INSERT INTO public.permission_role (role_id, permission_id) VALUES (1, 1);
INSERT INTO public.permission_role (role_id, permission_id) VALUES (1, 2);
INSERT INTO public.permission_role (role_id, permission_id) VALUES (1, 3);
INSERT INTO public.permission_role (role_id, permission_id) VALUES (1, 4);
INSERT INTO public.permission_role (role_id, permission_id) VALUES (2, 3);
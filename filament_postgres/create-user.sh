#!/bin/bash

echo "host all all 0.0.0.0/0 trust" >> /var/lib/postgresql/data/pg_hba.conf

echo "******CREATING FILAMENT DATABASE******"
gosu postgres postgres --single <<- EOSQL
   CREATE DATABASE filament;
   CREATE USER filament;
   GRANT ALL PRIVILEGES ON DATABASE filament to filament;
EOSQL
echo ""
echo "******FILAMENT DATABASE CREATED******"

echo "******Creating Nodes Table******"
gosu postgres postgres --single filament <<- EOSQL
     CREATE TABLE nodes (\
            id bigint NOT NULL,\
            field1 bigint NOT NULL,\
            field2 text NOT NULL,\
            field3 bigint NOT NULL\
     );

     ALTER TABLE public.nodes OWNER TO filament;

     CREATE SEQUENCE nodes_id_seq\
            INCREMENT BY 1\
            NO MAXVALUE\
            NO MINVALUE\
            CACHE 1;

     ALTER TABLE public.nodes_id_seq OWNER TO filament;

     ALTER SEQUENCE nodes_id_seq OWNED BY nodes.id;

     ALTER TABLE nodes ALTER COLUMN id SET DEFAULT nextval('nodes_id_seq'::regclass);

     ALTER TABLE ONLY nodes\
         ADD CONSTRAINT nodes_pkey PRIMARY KEY (id);

     CREATE INDEX nodes_field1_index ON nodes USING btree (field1);

     CREATE INDEX nodes_field2_index ON nodes USING btree (field2);

     CREATE INDEX nodes_field3_index ON nodes USING btree (field3);
EOSQL
echo ""
echo "********Nodes Table Created********"

echo "********Creating Edges Table*******"
gosu postgres postgres --single filament <<- EOSQL
CREATE TABLE edges (\
    id bigint NOT NULL,\
    field1 bigint NOT NULL,\
    field2 text NOT NULL,\
    field3 bigint NOT NULL\
);

ALTER TABLE public.edges OWNER TO postgres;

CREATE SEQUENCE edges_id_seq\
    INCREMENT BY 1\
    NO MAXVALUE\
    NO MINVALUE\
    CACHE 1;

ALTER TABLE public.edges_id_seq OWNER TO postgres;

ALTER SEQUENCE edges_id_seq OWNED BY edges.id;

ALTER TABLE edges ALTER COLUMN id SET DEFAULT nextval('edges_id_seq'::regclass);

ALTER TABLE ONLY edges\
    ADD CONSTRAINT edges_pkey PRIMARY KEY (id);

CREATE INDEX edges_field1_index ON edges USING btree (field1);

CREATE INDEX edges_field2_index ON edges USING btree (field2);

CREATE INDEX edges_field3_index ON edges USING btree (field3);
EOSQL
echo ""
echo "********Edges Table Created********"

echo "********Creating def_props Table********"
gosu postgres postgres --single filament <<- EOSQL

CREATE TABLE def_props (\
    id bigint NOT NULL,\
    field1 bigint NOT NULL,\
    field2 text NOT NULL,\
    field3 text NOT NULL,\
    lg_field3 text\
);

ALTER TABLE public.def_props OWNER TO postgres;

CREATE SEQUENCE def_props_id_seq\
    START WITH 1\
    INCREMENT BY 1\
    NO MAXVALUE\
    NO MINVALUE\
    CACHE 1;

ALTER TABLE public.def_props_id_seq OWNER TO postgres;

ALTER SEQUENCE def_props_id_seq OWNED BY def_props.id;

ALTER TABLE def_props ALTER COLUMN id SET DEFAULT nextval('def_props_id_seq'::regclass);

ALTER TABLE ONLY def_props\
    ADD CONSTRAINT def_props_pkey PRIMARY KEY (id);

ALTER TABLE ONLY def_props\
    ADD CONSTRAINT uniq_prop UNIQUE (field1, field2);

CREATE INDEX def_props_field1_index ON def_props USING btree (field1);

CREATE INDEX def_props_field2_index ON def_props USING btree (field2);

CREATE INDEX def_props_field3_index ON def_props USING btree (field3);

EOSQL
echo ""
echo "********def_props Table Created*********"

echo "**************SETTING PERMISSIONS**********"
gosu postgres postgres --single filament <<- EOSQL

GRANT ALL on def_props to filament;
GRANT ALL on def_props_id_seq to filament;
GRANT ALL on edges to filament;
GRANT ALL on edges_id_seq to filament;
GRANT ALL on nodes to filament;
GRANT ALL on nodes_id_seq to filament;

EOSQL
echo ""
echo "***********FINISHED SETTING PERMISSIONS********"

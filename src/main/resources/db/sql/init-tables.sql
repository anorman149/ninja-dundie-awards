-- 1. Organizations Table
CREATE TABLE organizations
(
    id                 UUID PRIMARY KEY,
    created_date       TIMESTAMP WITH TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by         VARCHAR(128),
    last_modified_by   VARCHAR(128),
    name               VARCHAR(255),
    CONSTRAINT ux_organization_name UNIQUE (name)
);

CREATE INDEX ix_organization_name ON organizations (name);

-- 2. Employees Table
CREATE TABLE employees
(
    id                 UUID PRIMARY KEY,
    created_date       TIMESTAMP WITH TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by         VARCHAR(128),
    last_modified_by   VARCHAR(128),
    first_name         VARCHAR(255),
    last_name          VARCHAR(255),
    dundie_awards      INTEGER,
    organization_id    UUID,
    CONSTRAINT fk_employees_organization_id
        FOREIGN KEY (organization_id)
            REFERENCES organizations (id)
            ON DELETE SET NULL
);

-- Index for the ManyToOne relationship optimization
CREATE INDEX ix_employee_organization_id ON employees (organization_id);

-- 3. Activities Table
CREATE TABLE activities
(
    id                 UUID PRIMARY KEY,
    created_date       TIMESTAMP WITH TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by         VARCHAR(128),
    last_modified_by   VARCHAR(128),
    occurred_at        TIMESTAMP,
    event              VARCHAR(255)
);

CREATE INDEX ix_activity_occurred_at ON activities (occurred_at);

-- Global Envers Revision Info Table
-- 1. Create the explicit sequence Hibernate is looking for
CREATE SEQUENCE revinfo_seq
    START WITH 1
    INCREMENT BY 50;
-- Hibernate's default optimizer size is 50

-- 2. Define the revinfo table using the sequence
CREATE TABLE revinfo
(
    rev      BIGINT PRIMARY KEY DEFAULT nextval('revinfo_seq'),
    revtstmp BIGINT
);

-- 3. Bind the sequence to the column so it drops cleanly if you ever drop the table
ALTER SEQUENCE revinfo_seq OWNED BY revinfo.rev;

-- Organizations Audit Table
CREATE TABLE organizations_aud
(
    id                 UUID     NOT NULL,
    rev                BIGINT   NOT NULL,
    revtype            SMALLINT NOT NULL,
    created_date       TIMESTAMP WITH TIME ZONE,
    last_modified_date TIMESTAMP WITH TIME ZONE,
    created_by         VARCHAR(128),
    last_modified_by   VARCHAR(128),
    name               VARCHAR(255),
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_organizations_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

-- Employees Audit Table
CREATE TABLE employees_aud
(
    id                 UUID     NOT NULL,
    rev                BIGINT   NOT NULL,
    revtype            SMALLINT NOT NULL,
    created_date       TIMESTAMP WITH TIME ZONE,
    last_modified_date TIMESTAMP WITH TIME ZONE,
    created_by         VARCHAR(128),
    last_modified_by   VARCHAR(128),
    first_name         VARCHAR(255),
    last_name          VARCHAR(255),
    dundie_awards      INTEGER,
    organization_id    UUID, -- Stored as a raw UUID since it's @NotAudited
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_employees_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo (rev)
);

-- Activities Audit Table
CREATE TABLE activities_aud
(
    id                 UUID     NOT NULL,
    rev                BIGINT   NOT NULL,
    revtype            SMALLINT NOT NULL,
    created_date       TIMESTAMP WITH TIME ZONE,
    last_modified_date TIMESTAMP WITH TIME ZONE,
    created_by         VARCHAR(128),
    last_modified_by   VARCHAR(128),
    occurred_at        TIMESTAMP,
    event              VARCHAR(255),
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_activities_aud_revinfo FOREIGN KEY (rev) REFERENCES revinfo (rev)
);
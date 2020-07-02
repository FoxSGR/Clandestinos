-- @DELIMITER ;

# ClanPlayer DDL

CREATE TABLE IF NOT EXISTS player
(
    id                   VARCHAR(36) PRIMARY KEY,
    player_friendly_fire BOOLEAN,
    kill_count           INTEGER,
    death_count          INTEGER,
    tag                  VARCHAR(10)
);

# Clan DDL

CREATE TABLE IF NOT EXISTS clan
(
    tag                VARCHAR(10) PRIMARY KEY,
    styled_tag         VARCHAR(20) UNIQUE NOT NULL,
    clan_name          VARCHAR(40)        NOT NULL,
    clan_friendly_fire BOOLEAN            NOT NULL,
    clan_owner         VARCHAR(36) UNIQUE NOT NULL,
    FOREIGN KEY (clan_owner) REFERENCES player (id)
);

# ALTER TABLE player
#     ADD FOREIGN KEY (tag) REFERENCES clan (tag);

CREATE TABLE IF NOT EXISTS clan_leader
(
    tag       VARCHAR(10) NOT NULL,
    leader_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (tag, leader_id),
    FOREIGN KEY (tag) REFERENCES clan (tag),
    FOREIGN KEY (leader_id) REFERENCES player (id)
);

CREATE TABLE IF NOT EXISTS clan_member
(
    tag       VARCHAR(10) NOT NULL,
    member_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (tag, member_id),
    FOREIGN KEY (tag) REFERENCES clan (tag),
    FOREIGN KEY (member_id) REFERENCES player (id)
);

CREATE TABLE IF NOT EXISTS clan_enemy
(
    tag       VARCHAR(10) REFERENCES clan (tag),
    enemy_tag VARCHAR(10) REFERENCES clan (tag),
    PRIMARY KEY (tag, enemy_tag)
);

# Invite DDL

CREATE TABLE IF NOT EXISTS invite
(
    id             VARCHAR(100) PRIMARY KEY,
    invited_to_tag VARCHAR(10) NOT NULL,
    invited_player VARCHAR(36) NOT NULL,
    FOREIGN KEY (invited_to_tag) REFERENCES player (id),
    FOREIGN KEY (invited_player) REFERENCES player (id)
);

# Neutrality Request DDL

CREATE TABLE IF NOT EXISTS neutrality_request
(
    id            VARCHAR(100) PRIMARY KEY,
    requester_tag VARCHAR(10) NOT NULL,
    requestee_tag VARCHAR(10) NOT NULL,
    FOREIGN KEY (requester_tag) REFERENCES clan (tag),
    FOREIGN KEY (requestee_tag) REFERENCES clan (tag)
);

COMMIT;

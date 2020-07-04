-- @DELIMITER $$
DELIMITER $$

DROP PROCEDURE IF EXISTS find_clan
$$

CREATE PROCEDURE find_clan(target_tag VARCHAR(20))
BEGIN
    SELECT c.tag                AS tag,
           c.styled_tag         AS styled_tag,
           c.clan_name          AS clan_name,
           c.clan_owner         AS clan_owner,
           c.clan_friendly_fire AS clan_friendly_fire,
           cl.leader_id         AS leader_id,
           cm.member_id         AS member_id,
           ce.enemy_tag         AS enemy_tag
    FROM clan c
             LEFT JOIN clan_leader cl ON c.tag = cl.tag
             LEFT JOIN clan_member cm ON c.tag = cm.tag
             LEFT JOIN clan_enemy ce ON c.tag = ce.tag
    WHERE c.tag = target_tag;
END
$$

DROP PROCEDURE IF EXISTS find_all_clans
$$

CREATE PROCEDURE find_all_clans()
BEGIN
    SELECT c.tag                AS tag,
           c.styled_tag         AS styled_tag,
           c.clan_name          AS clan_name,
           c.clan_owner         AS clan_owner,
           c.clan_friendly_fire AS clan_friendly_fire,
           cl.leader_id         AS leader_id,
           cm.member_id         AS member_id,
           ce.enemy_tag         AS enemy_tag
    FROM clan c
             LEFT JOIN clan_leader cl ON c.tag = cl.tag
             LEFT JOIN clan_member cm ON c.tag = cm.tag
             LEFT JOIN clan_enemy ce ON c.tag = ce.tag;
END
$$

DROP PROCEDURE IF EXISTS save_clan
$$

CREATE PROCEDURE save_clan(p_tag VARCHAR(10),
                           p_styled_tag VARCHAR(40),
                           p_clan_name VARCHAR(40),
                           p_clan_friendly_fire BOOLEAN,
                           p_clan_owner VARCHAR(36),
                           p_clan_leaders VARCHAR(512),
                           p_clan_members VARCHAR(1024),
                           p_clan_enemies VARCHAR(512),
                           p_update_if_exists BOOLEAN)
BEGIN
    DECLARE member VARCHAR(36);

    IF p_update_if_exists THEN
        INSERT INTO clan
        VALUES (p_tag, p_styled_tag, p_clan_name, p_clan_friendly_fire, p_clan_owner)
        ON DUPLICATE KEY UPDATE tag                = p_tag,
                                styled_tag         = p_styled_tag,
                                clan_name          = p_clan_name,
                                clan_friendly_fire = p_clan_friendly_fire,
                                clan_owner         = p_clan_owner;
    ELSE
        INSERT INTO clan
        VALUES (p_tag, p_styled_tag, p_clan_name, p_clan_friendly_fire, p_clan_owner);
    END IF;

    DELETE FROM clan_leader WHERE tag = p_tag;
    WHILE p_clan_leaders != ''
        DO
            SET member = SUBSTRING_INDEX(p_clan_leaders, ',', 1);
            INSERT INTO clan_leader (tag, leader_id) VALUES (p_tag, member);

            IF LOCATE(',', p_clan_leaders) > 0 THEN
                SET p_clan_leaders = SUBSTRING(p_clan_leaders, LOCATE(',', p_clan_leaders) + 1);
            ELSE
                SET p_clan_leaders = '';
            END IF;
        END WHILE;

    DELETE FROM clan_member WHERE tag = p_tag;
    WHILE p_clan_members != ''
        DO
            SET member = SUBSTRING_INDEX(p_clan_members, ',', 1);
            INSERT INTO clan_member (tag, member_id) VALUES (p_tag, member);

            IF LOCATE(',', p_clan_members) > 0 THEN
                SET p_clan_members = SUBSTRING(p_clan_members, LOCATE(',', p_clan_members) + 1);
            ELSE
                SET p_clan_members = '';
            END IF;
        END WHILE;

    DELETE FROM clan_enemy WHERE tag = p_tag;
    WHILE p_clan_enemies != ''
        DO
            SET member = SUBSTRING_INDEX(p_clan_enemies, ',', 1);
            INSERT INTO clan_enemy (tag, enemy_tag) VALUES (p_tag, member);

            IF LOCATE(',', p_clan_members) > 0 THEN
                SET p_clan_enemies = SUBSTRING(p_clan_enemies, LOCATE(',', p_clan_enemies) + 1);
            ELSE
                SET p_clan_enemies = '';
            END IF;
        END WHILE;
END
$$

DROP PROCEDURE IF EXISTS remove_clan
$$

CREATE PROCEDURE remove_clan(p_tag VARCHAR(10))
BEGIN
    DELETE FROM clan_enemy WHERE tag = LOWER(p_tag);
    DELETE FROM clan_member WHERE tag = LOWER(p_tag);
    DELETE FROM clan_leader WHERE tag = LOWER(p_tag);
    DELETE FROM clan WHERE tag = LOWER(p_tag);
END
$$

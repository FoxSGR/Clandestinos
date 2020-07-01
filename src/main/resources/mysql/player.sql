-- @DELIMITER $$
DELIMITER $$

DROP PROCEDURE IF EXISTS update_player
$$

CREATE PROCEDURE update_player(p_id VARCHAR(36),
                               p_friendly_fire BOOLEAN,
                               p_kill_count INTEGER,
                               p_death_count INTEGER,
                               p_clan_tag VARCHAR(10))
BEGIN
    UPDATE player
    SET player_friendly_fire = p_friendly_fire,
        kill_count           = p_kill_count,
        death_count          = p_death_count,
        tag                  = p_clan_tag
    WHERE id = p_id;
END
$$

package foxsgr.clandestinos.domain.builder;

import foxsgr.clandestinos.domain.model.clan.Clan;

import java.util.HashSet;
import java.util.Set;

public class ClanBuilder {

    private String tag;
    private String name;
    private boolean friendlyFireEnabled;
    private String owner;
    private Set<String> leaders;
    private Set<String> members;
    private Set<String> enemyClans;

    public ClanBuilder() {
        leaders = new HashSet<>();
        members = new HashSet<>();
        enemyClans = new HashSet<>();
    }

    public ClanBuilder withTag(String tag) {
        this.tag = tag;
        return this;
    }

    public ClanBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ClanBuilder withFriendlyFireEnabled(boolean friendlyFireEnabled) {
        this.friendlyFireEnabled = friendlyFireEnabled;
        return this;
    }

    public ClanBuilder withOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public ClanBuilder addLeader(String leader) {
        this.leaders.add(leader);
        return this;
    }

    public ClanBuilder addMember(String member) {
        this.members.add(member);
        return this;
    }

    public ClanBuilder addEnemy(String enemyTag) {
        this.enemyClans.add(enemyTag);
        return this;
    }

    public String getTag() {
        return tag;
    }

    public Clan build() {
        return new Clan(
                tag,
                name,
                owner,
                leaders,
                members,
                enemyClans,
                friendlyFireEnabled
        );
    }
}

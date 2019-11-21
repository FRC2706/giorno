package ca.team2706.giorno;

import android.app.Application;

import ca.team2706.giorno.provider.CompetitionProvider;
import ca.team2706.giorno.provider.MatchProvider;
import ca.team2706.giorno.provider.ScheduleProvider;
import ca.team2706.giorno.provider.TeamProvider;

public class GiornoApplication extends Application {

	public final CompetitionProvider competitionProvider;
	public final TeamProvider teamProvider;
	public final ScheduleProvider scheduleProvider;
	public final MatchProvider matchProvider;

	public GiornoApplication() {
		super();
		competitionProvider = new CompetitionProvider();
		teamProvider = new TeamProvider();
		scheduleProvider = new ScheduleProvider();
		matchProvider = new MatchProvider();
	}

}

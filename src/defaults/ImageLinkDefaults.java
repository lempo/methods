package defaults;

import java.util.HashMap;
import java.util.Map;

public class ImageLinkDefaults {
	private static ImageLinkDefaults instance;

	public static ImageLinkDefaults getInstance() {
		if (instance == null) {
			instance = new ImageLinkDefaults();
		}
		return instance;
	}

	public enum Key {
		FIRSTSCREEN,
		BACKGROUND,
		CLOSE,
		RESTORE,
		HIDE,
		CLOSE_ROLLOVER,
		RESTORE_ROLLOVER,
		HIDE_ROLLOVER,
		MAIN_MENU_EXIT, 
		MAIN_MENU_ABOUT, 
		MAIN_MENU_HELP, 
		MAIN_MENU_TASKS, 
		MAIN_MENU_EXIT_ROLLOVER, 
		MAIN_MENU_ABOUT_ROLLOVER, 
		MAIN_MENU_HELP_ROLLOVER, 
		MAIN_MENU_TASKS_ROLLOVER,
		LOGO,
		INCREASE,
		DECREASE_HORIZONTAL,
		DECREASE,
		INCREASE_HORIZONTAL,
		ARROW,
		CIRCLE_ARROW,
		BEGIN,
		REPEAT,
		REPEAT_ROLLOVER,
		MENU,
		RADIO,
		RADIO_SELECTED,
		RAVEN,
		EYSENCK,
		EXCEPTION,
		KOMPLIMED,
		COPYRIGHT;
	}

	private Map<Key, String> links;

	private ImageLinkDefaults() {
		links = new HashMap<Key, String>();

		links.put(Key.FIRSTSCREEN, "resources/image/background1.png");
		links.put(Key.BACKGROUND, "resources/image/background.png");
		links.put(Key.CLOSE, "resources/image/close.png");
		links.put(Key.RESTORE, "resources/image/restore.png");
		links.put(Key.HIDE, "resources/image/hide.png");
		links.put(Key.CLOSE_ROLLOVER, "resources/image/close_rollover.png");
		links.put(Key.RESTORE_ROLLOVER, "resources/image/restore_rollover.png");
		links.put(Key.HIDE_ROLLOVER, "resources/image/hide_rollover.png");
		links.put(Key.MAIN_MENU_EXIT, "resources/image/exit.png");
		links.put(Key.MAIN_MENU_ABOUT, "resources/image/about.png");
		links.put(Key.MAIN_MENU_HELP, "resources/image/help.png");
		links.put(Key.MAIN_MENU_TASKS, "resources/image/tasks.png");
		links.put(Key.MAIN_MENU_EXIT_ROLLOVER, "resources/image/exit_rollover.png");
		links.put(Key.MAIN_MENU_ABOUT_ROLLOVER, "resources/image/about_rollover.png");
		links.put(Key.MAIN_MENU_HELP_ROLLOVER, "resources/image/help_rollover.png");
		links.put(Key.MAIN_MENU_TASKS_ROLLOVER, "resources/image/tasks_rollover.png");
		links.put(Key.LOGO, "resources/image/logo.png");
		links.put(Key.INCREASE, "resources/image/increase.png");
		links.put(Key.DECREASE_HORIZONTAL, "resources/image/decrease_horizontal.png");
		links.put(Key.DECREASE, "resources/image/decrease.png");
		links.put(Key.INCREASE_HORIZONTAL, "resources/image/increase_horizontal.png");
		links.put(Key.ARROW, "resources/image/arrow.png");
		links.put(Key.CIRCLE_ARROW, "resources/image/circleArrow.png");
		links.put(Key.BEGIN, "resources/image/begin.png");
		links.put(Key.REPEAT, "resources/image/repeat.png");
		links.put(Key.REPEAT_ROLLOVER, "resources/image/repeat_rollover.png");
		links.put(Key.MENU, "resources/image/menu");
		links.put(Key.RADIO, "resources/image/radio_unselected.png");
		links.put(Key.RADIO_SELECTED, "resources/image/radio_selected.png");
		links.put(Key.RAVEN, "resources/image/raven/");
		links.put(Key.EYSENCK, "resources/image/eysenck/");
		links.put(Key.EXCEPTION, "resources/image/exception/");
		links.put(Key.KOMPLIMED, "resources/image/komplimed.png");
		links.put(Key.COPYRIGHT, "resources/image/copyright.png");
	}

	public String getLink(Key key) {
		return links.get(key);
	}

	public void setLink(Key key, String link) {
		links.put(key, link);
	}
}

package defaults;

import java.util.HashMap;
import java.util.Map;

public class TextLinkDefaults {
	private static TextLinkDefaults instance;

	public static TextLinkDefaults getInstance() {
		if (instance == null) {
			instance = new TextLinkDefaults();
		}
		return instance;
	}

	public enum Key {
		FAQ, 
		ABOUT, 
		GROUPS, 
		WELCOME, 
		CATEGORIES,
		DICTIONARY,
		LISTEN,
		TRAVEL,
		WORDS,
		INTERFACE,
		FIGURES,
		VERSION,
		SERVER,
		TAYLOR,
		AUDIT,
		MUNSTERBERG,
		CORRECTION,
		DESCRIPTOR,
		EYSENCK,
		RAVEN,
		SAN,
		EXCEPTION,
		SPILBERG,
		CLASSIFICATION;
	}

	private Map<Key, String> links;

	private TextLinkDefaults() {
		links = new HashMap<Key, String>();

		links.put(Key.FAQ, "resources/text/faq.html");
		links.put(Key.ABOUT, "resources/text/about.html");
		links.put(Key.GROUPS, "resources/text/groups.xml");
		links.put(Key.WELCOME, "resources/text/welcome.html");
		links.put(Key.CATEGORIES, "resources/text/words/categories.xml");
		links.put(Key.DICTIONARY, "resources/text/dictionary/");
		links.put(Key.LISTEN, "resources/text/listen/listen.xml");
		links.put(Key.TRAVEL, "resources/text/travel/regions.xml");
		links.put(Key.WORDS, "resources/text/words/categories.xml");
		links.put(Key.INTERFACE, "resources/text/interface.xml");
		links.put(Key.FIGURES, "resources/text/figures/");
		links.put(Key.VERSION, "resources/text/version.xml");
		links.put(Key.SERVER, "resources/text/server.xml");
		links.put(Key.TAYLOR, "resources/text/tests/taylor.xml");
		links.put(Key.AUDIT, "resources/text/tests/audit.xml");
		links.put(Key.MUNSTERBERG, "resources/text/tests/munsterberg.xml");
		links.put(Key.CORRECTION, "resources/text/tests/correction.xml");
		links.put(Key.DESCRIPTOR, "resources/text/tests/descriptor.xml");
		links.put(Key.EYSENCK, "resources/text/tests/eysenck.xml");
		links.put(Key.RAVEN, "resources/text/tests/raven.xml");
		links.put(Key.SAN, "resources/text/tests/san.xml");
		links.put(Key.EXCEPTION, "resources/text/tests/exception.xml");
		links.put(Key.SPILBERG, "resources/text/tests/spilberg.xml");
		links.put(Key.CLASSIFICATION, "resources/text/tests/classification.xml");
	}

	public String getLink(Key key) {
		return links.get(key);
	}

	public void setLink(Key key, String link) {
		links.put(key, link);
	}
}

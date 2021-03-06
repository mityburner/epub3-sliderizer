package danielweck.epub3.sliderizer.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TimeZone;

import com.google.common.base.Function;

import danielweck.epub3.sliderizer.Epub3FileSet;
import danielweck.epub3.sliderizer.NavDoc;
import danielweck.epub3.sliderizer.Print;
import danielweck.epub3.sliderizer.XHTML;

public final class SlideShow extends Fielder {

	// Forwards for Mustache context
    
	public final static String GENERATOR = Epub3FileSet.GENERATOR;
	public final static String KEYWORDS = Epub3FileSet.KEYWORDS;
	public final static String FOLDER_IMG = Epub3FileSet.FOLDER_IMG;
	public final static String FOLDER_FONTS = Epub3FileSet.FOLDER_FONTS;
	public final static String FOLDER_CSS = Epub3FileSet.FOLDER_CSS;
	public final static String FOLDER_JS = Epub3FileSet.FOLDER_JS;
	public final static String FOLDER_HTML = Epub3FileSet.FOLDER_HTML;
	public final static String FOLDER_CUSTOM = Epub3FileSet.FOLDER_CUSTOM;
	public final static Epub3FileSet.FileId[] CSSs = Epub3FileSet.CSSs;
	public final static Epub3FileSet.FileId CSS_NAVDOC = Epub3FileSet.CSS_NAVDOC;
	public final static Epub3FileSet.FileId[] CSS_FONTS = Epub3FileSet.CSS_FONTS;
	public final static Epub3FileSet.FileId[] JSs = Epub3FileSet.JSs;
	public final static String FIRST_SLIDE_FILENAME = XHTML.getFileName(1);
	public final static String NAV_SLIDE_FILENAME = NavDoc.getFileName();
	public final static String PRINT_FILENAME = Print.getFileName();

	public final String LAST_SLIDE_FILENAME() {
		return XHTML.getFileName(slides.size());
	}

	public final static Function<String, String> backgroundImageCss = XHTML.backgroundImageCss;

	SlideShow() throws Exception {

		TimeZone timeZone = TimeZone.getTimeZone("UTC");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		dateFormat.setTimeZone(timeZone);
		DATE = dateFormat.format(new Date());

		// DATE = String.format("%tFT%<tRZ", new Date());
	}

	private String baseFolderPath = null;

	public String getBaseFolderPath() {
		return baseFolderPath;
	}

	public SlideShow(String folderPath) throws Exception {

		this();
		baseFolderPath = folderPath;
	}

	public String REFLOWABLE = null;

	public boolean FIXED() {
		if (REFLOWABLE == null) {
			return true;
		}

		return !REFLOWABLE.equalsIgnoreCase("yes")
				&& !REFLOWABLE.equalsIgnoreCase("true")
				&& !REFLOWABLE.equalsIgnoreCase("1");
	}

	public boolean REFLOW() {
		return !FIXED();
	}

	public String PAGE_SPREAD = null;

	public boolean TWO_PAGE_SYNTHETIC_SPREAD() {
		if (PAGE_SPREAD == null) {
			return false;
		}

		return PAGE_SPREAD.equalsIgnoreCase("yes")
				|| PAGE_SPREAD.equalsIgnoreCase("true")
				|| PAGE_SPREAD.equalsIgnoreCase("1");
	}

	public String URL_POSTER = "https://danielweck.github.io/epub3-sliderizer/doc/epub/img/custom/assets/EPUB3-Sliderizer.png";
	public String URL_ROOT = "https://danielweck.github.io/epub3-sliderizer/doc/";
    
	public String DATE = null;

	public String TITLE = "DEFAULT TITLE";
	public String SUBTITLE = null;

	public String SUMMARY = null;
	public String DESCRIPTION = null;
	public String SUBJECT = null;

	public String CREATOR = Epub3FileSet.GENERATOR;
	public String PUBLISHER = null;

	public String IDENTIFIER = "DEFAULT-UID";

	public String LANGUAGE = "en-US";
	public String PAGE_DIR = "ltr";

	public String RIGHTS = null;
	public String LICENSE = null;

	public String VIEWPORT_WIDTH = "1290";
	public String VIEWPORT_HEIGHT = "1000";

	public String FONT_SIZE = "40";

	public String FAVICON = "favicon.ico";

	public String MO_NARRATOR = null;
	public String MO_DUR = null;
	public String MO_ACTIVE = null;
	public String MO_PLAYBACK_ACTIVE = null;
	public String MO_AUDIO_FILES = null;

	public String FAVICON_FOLDER() {
		return FOLDER_IMG
				+ (this.FAVICON.equals("favicon.ico") ? "" : "/"
						+ FOLDER_CUSTOM);
	}

	public String EPUB_FILE() {
		return FILE_EPUB != null ? FILE_EPUB : "EPUB3.epub";
	}

	public String TOUCHICON = null;

	public String LOGO = null;

	public String COVER = null;

	public String FILE_EPUB = null;

	public String INCREMENTALS = "NO";

	public boolean incrementalsManual() {
		if (INCREMENTALS == null) {
			return false;
		}
		return INCREMENTALS.equalsIgnoreCase("TRUE")
				|| INCREMENTALS.equalsIgnoreCase("YES")
				|| INCREMENTALS.equalsIgnoreCase("1");
	}

	public boolean incrementalsAuto() {
		if (INCREMENTALS == null) {
			return false;
		}
		return INCREMENTALS.equalsIgnoreCase("AUTO");
	}

	// TODO: MASSIIIIVE HACK!! :(
	public static int _verbosity = -1;

	public String CSS_STYLE = null;

	public String CSS_STYLING() throws Exception {
		return Epub3FileSet.processCssStyle(this, null, CSS_STYLE, pathEpubFolder, SlideShow._verbosity);
	}

	public String JS_SCRIPT = null;

	// TODO: yucky yuck
	public String pathEpubFolder = null;

	public String NOTES = null;

	public String NOTES_XHTML() throws Exception {

		if (NOTES == null) {
			return null;
		}

		return XHTML.massage(NOTES, this, null, pathEpubFolder, -1);
	}

	public final ArrayList<Slide> slides = new ArrayList<Slide>();

	public String FILES_FONT = null;

	public String FILES_IMG = null;

	public String BACKGROUND_AUDIO = null;

	public String BACKGROUND_IMG = null;
	public String BACKGROUND_IMG_SIZE = "contain"; // auto, contain, cover, 100%
													// 100%

	public String FILES_CSS = null;

	private ArrayList<String> _xCSSs = null;

	public ArrayList<String> xCSSs() {

		if (FILES_CSS == null) {
			return null;
		}
		if (_xCSSs != null) {
			return _xCSSs;
		}

		ArrayList<String> array = Epub3FileSet.splitPaths(FILES_CSS);

		_xCSSs = new ArrayList<String>(array.size());

		for (String path : array) {

			if (_xCSSs.contains(path)) {
				continue;
			}
			_xCSSs.add(path);
		}

		if (_xCSSs.size() == 0) {
			_xCSSs = null;
		}
		return _xCSSs;
	}

	public String FILES_CSS_FONTS = null;

	private ArrayList<String> _xCSSs_FONTS = null;

	public ArrayList<String> xCSSs_FONTS() {

		if (FILES_CSS_FONTS == null) {
			return null;
		}
		if (_xCSSs_FONTS != null) {
			return _xCSSs_FONTS;
		}

		ArrayList<String> array = Epub3FileSet.splitPaths(FILES_CSS_FONTS);

		_xCSSs_FONTS = new ArrayList<String>(array.size());

		for (String path : array) {

			if (_xCSSs_FONTS.contains(path)) {
				continue;
			}
			_xCSSs_FONTS.add(path);
		}

		if (_xCSSs_FONTS.size() == 0) {
			_xCSSs_FONTS = null;
		}
		return _xCSSs_FONTS;
	}

	public String FILES_JS = null;

	private ArrayList<String> _xJSs = null;

	public ArrayList<String> xJSs() {

		if (FILES_JS == null) {
			return null;
		}
		if (_xJSs != null) {
			return _xJSs;
		}

		ArrayList<String> array = Epub3FileSet.splitPaths(FILES_JS);

		_xJSs = new ArrayList<String>(array.size());

		for (String path : array) {

			if (_xJSs.contains(path)) {
				continue;
			}
			_xJSs.add(path);
		}

		if (_xJSs.size() == 0) {
			_xJSs = null;
		}
		return _xJSs;
	}

	public void setDimensions(int width, int height) {

		int originalWidth = Integer.parseInt(VIEWPORT_WIDTH);
		float ratio = originalWidth / (float) width;

		int originalFontSize = Integer.parseInt(FONT_SIZE);
		float size = originalFontSize / ratio;

		FONT_SIZE = "" + Math.ceil(size);
		VIEWPORT_WIDTH = ("" + width);
		VIEWPORT_HEIGHT = ("" + height);
	}

	private ArrayList<String> allReferences_IMG = null;

	public void addReferences_IMG(String imgs) {
		if (allReferences_IMG == null) {
			return;
		}
		ArrayList<String> array = Epub3FileSet.splitPaths(imgs);
		for (String str : array) {
			if (!allReferences_IMG.contains(str)) {
				allReferences_IMG.add(str);
			}
		}
	}

	public ArrayList<String> getAllReferences_IMG() {

		if (allReferences_IMG == null) {
			allReferences_IMG = new ArrayList<String>();

			addReferences_IMG(this.LOGO);
			addReferences_IMG(this.TOUCHICON);
			addReferences_IMG(this.COVER);
			addReferences_IMG(this.FILES_IMG);
			addReferences_IMG(this.BACKGROUND_IMG);

			for (Slide slide : slides) {
				addReferences_IMG(slide.FILES_IMG);
				addReferences_IMG(slide.BACKGROUND_IMG);
			}
		}

		return allReferences_IMG;
	}

	public void toString(Appendable appendable) throws Exception {

		appendable.append("***** SLIDESHOW");
		appendable.append('\n');

		super.toString(appendable);

		int i = 0;
		for (Slide slide : slides) {
			appendable.append('\n');

			i++;
			slide.toString(appendable, i);
		}
	}

	protected boolean parseSpecial(File file, String line,
			Stack<BufferedReader> bufferedReaders,
			Map<BufferedReader, String> mapBufferedReaderLine, int verbosity)
			throws Exception {

		// HACK!
		SlideShow._verbosity = verbosity;

		boolean isSlideMarker = line.equals(Slide.SLIDE_MARKER);
		while (isSlideMarker) {

			Slide slide = Slide.parse(file, this, bufferedReaders,
					mapBufferedReaderLine, verbosity);
			slides.add(slide);

			if (!bufferedReaders.isEmpty()) {
				BufferedReader bufferedReader = bufferedReaders.peek();
				if ((isSlideMarker = bufferedReader.ready())) {
					continue;
				}
			}

			return true;
		}

		return false;
	}

	public boolean importedConverted = false;

	public static SlideShow parse(String uriDataFile, int verbosity)
			throws Exception {

		// HACK!
		SlideShow._verbosity = verbosity;

		URI uri = new URI(uriDataFile);
		if (!uri.getScheme().equalsIgnoreCase("file")) {
			throw new MalformedURLException(uriDataFile);
		}

		//String uriPath = uri.toString();
		String uriPath = uri.getPath();
		
		File file = new File(uriPath);
		if (!file.exists()) {
			throw new FileNotFoundException(uriPath);
		}

		if (verbosity > 0) {
			System.out.println(" ");
			System.out.println(">>>>> PARSING: " + file.getAbsolutePath());
		}

		SlideShow slideShow = new SlideShow(file.getParent());

		String ext = Epub3FileSet.getFileExtension(file.getAbsolutePath());
		if (ext.equalsIgnoreCase("opf")) {
			EPubImporter.parse(slideShow, file, verbosity);
		} else if (ext.equalsIgnoreCase("html")) {
			DZSlidesImporter.parse(slideShow, file, verbosity);
		} else {
			BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(
						new FileInputStream(file), "UTF-8")
				// new FileReader(file)
				);

				Stack<BufferedReader> bufferedReaders = new Stack<BufferedReader>();
				bufferedReaders.push(bufferedReader);

				Map<BufferedReader, String> mapBufferedReaderLine = new HashMap<BufferedReader, String>();

				parseFields(file, slideShow, bufferedReaders,
						mapBufferedReaderLine, verbosity);
			} finally {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			}
		}

		if (verbosity > 0) {
			System.out.println(" ");
			System.out.println(">>>>> PARSED:");
			System.out.println(slideShow.toString());
		}

		return slideShow;
	}

	private static void repeatChar(char c, int n, Writer out) throws Exception {
		for (int i = 0; i < n; i++) {
			out.write(c);
		}
	}

	private void createSampleTemplate_Fields(Map<String, String> fields,
			Writer writer, int verbosity) throws Exception {
		for (Map.Entry<String, String> field : fields.entrySet()) {
			String fieldName = field.getKey();
			String defaultFieldValue = field.getValue();

			writer.write(Fielder.FIELD_PREFIX);
			writer.write(fieldName);
			writer.write('\n');
			writer.write(Fielder.COMMENT_PREFIX);
			// writer.write('(');
			if (defaultFieldValue == null) {
				writer.write("NULL");
			} else {
				boolean first = true;
				ArrayList<String> array = Epub3FileSet
						.splitPaths(defaultFieldValue);
				for (String path : array) {

					if (!first) {
						writer.write('\n');
						writer.write(Fielder.COMMENT_PREFIX);
					}

					writer.write(path);
					first = false;
				}
			}

			// writer.write(')');
			writer.write('\n');
			writer.write('\n');

			writer.flush();
		}
	}

	public void createSampleTemplate(Writer writer, int verbosity)
			throws Exception {

		if (verbosity > 0) {
			System.out.println(" ");
			System.out.println(">>>>> SAMPLE TEMPLATE: ");
		}

		writer.write('\n');

		writer.write(Fielder.COMMENT_PREFIX);
		String str = "This is a sample template slideshow data file";
		writer.write(str);
		writer.write('\n');

		writer.write(Fielder.COMMENT_PREFIX);
		writer.write("Format:");
		writer.write('\n');

		writer.write(Fielder.COMMENT_PREFIX);
		repeatChar(' ', 4, writer);
		writer.write("_FIELD_NAME");
		writer.write('\n');

		writer.write(Fielder.COMMENT_PREFIX);
		repeatChar(' ', 4, writer);
		writer.write(Fielder.COMMENT_PREFIX);
		writer.write("DEFAULT VALUE");
		writer.write('\n');

		writer.write(Fielder.COMMENT_PREFIX);
		repeatChar(' ', 4, writer);
		writer.write("[OVERRIDE BELOW WITH YOUR OWN VALUE]");
		writer.write('\n');
		writer.write(Fielder.COMMENT_PREFIX);
		repeatChar(' ', 4, writer);
		writer.write("[...WHICH CAN BE MULTILINE]");
		writer.write('\n');

		writer.write(Fielder.COMMENT_PREFIX);
		repeatChar('-', str.length(), writer);
		writer.write('\n');

		writer.write('\n');

		Map<String, String> fields = getFields();
		createSampleTemplate_Fields(fields, writer, verbosity);

		writer.write('\n');

		writer.write(Fielder.COMMENT_PREFIX);
		str = "Slide #1 example";
		writer.write(str);
		writer.write('\n');

		writer.write(Fielder.COMMENT_PREFIX);
		repeatChar('-', str.length(), writer);
		writer.write('\n');

		writer.write('\n');

		writer.write(Slide.SLIDE_MARKER);
		writer.write('\n');
		writer.write('\n');

		Slide slide = slides.isEmpty() ? new Slide() : slides.get(0);
		fields = slide.getFields();
		createSampleTemplate_Fields(fields, writer, verbosity);

		writer.write('\n');

		writer.write(Fielder.COMMENT_PREFIX);
		str = "Slide #2 example";
		writer.write(str);
		writer.write('\n');

		writer.write(Fielder.COMMENT_PREFIX);
		repeatChar('-', str.length(), writer);
		writer.write('\n');

		writer.write('\n');

		writer.write(Slide.SLIDE_MARKER);
		writer.write('\n');
		writer.write('\n');

		writer.flush();

		createSampleTemplate_Fields(fields, writer, verbosity);

		writer.write('\n');

		writer.flush();
	}
}

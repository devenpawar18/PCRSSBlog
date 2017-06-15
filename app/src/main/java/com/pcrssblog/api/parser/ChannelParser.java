package com.pcrssblog.api.parser;

import android.util.Xml;

import com.pcrssblog.api.model.Article;
import com.pcrssblog.api.model.Channel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * An XML Pull Parser to Parse RSS Feeds containing Channel
 */

public class ChannelParser {
    private static final String NULL_STRING = null;
    private static final String TAG_CHANNEL = "channel";
    private static final String TAG_ARTICLE = "item";
    private static final String TAG_CHANNEL_TITLE = "title";
    private static final String TAG_ARTICLE_TITLE = "title";
    private static final String TAG_ARTICLE_CONTENT = "description";
    private static final String TAG_ARTICLE_MEDIA = "media:content";
    private static final String TAG_ARTICLE_IMAGE_URL = "url";
    private static final String TAG_ARTICLE_WEB_URL = "link";

    /**
     * Initializes parser and starts reading channel containing articles
     *
     * @param pInputStream
     * @return Channel
     * @throws XmlPullParserException
     * @throws IOException
     */
    public Channel parse(final InputStream pInputStream) throws XmlPullParserException, IOException {
        try {
            final XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(pInputStream, null);
            parser.nextTag();
            parser.nextTag();
            return readChannel(parser);
        } finally {
            pInputStream.close();
        }
    }

    /**
     * Read Channel containing list of Articles
     *
     * @param pParser
     * @return Channel
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Channel readChannel(final XmlPullParser pParser) throws XmlPullParserException, IOException {
        String title = null;
        List<Article> articles = new ArrayList<>();

        pParser.require(XmlPullParser.START_TAG, NULL_STRING, TAG_CHANNEL);
        while (pParser.next() != XmlPullParser.END_TAG) {
            if (pParser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            final String name = pParser.getName();
            if (name.equals(TAG_CHANNEL_TITLE)) {
                title = readTextTag(pParser, TAG_CHANNEL_TITLE);
            } else if (name.equals(TAG_ARTICLE)) {
                articles.add(readArticle(pParser));
            } else {
                skip(pParser);
            }
        }

        return new Channel(title, articles);
    }

    /**
     * Read each article, so it can be added to the list
     *
     * @param parser
     * @return Article
     * @throws XmlPullParserException
     * @throws IOException
     */
    private Article readArticle(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NULL_STRING, TAG_ARTICLE);
        String title = null;
        String content = null;
        String imageURL = null;
        String webURL = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(TAG_ARTICLE_TITLE)) {
                title = readTextTag(parser, TAG_ARTICLE_TITLE);
            } else if (name.equals(TAG_ARTICLE_CONTENT)) {
                content = readTextTag(parser, TAG_ARTICLE_CONTENT);
            } else if (name.equals(TAG_ARTICLE_MEDIA)) {
                imageURL = readImageTag(parser);
            } else if (name.equals(TAG_ARTICLE_WEB_URL)) {
                webURL = readTextTag(parser, TAG_ARTICLE_WEB_URL);
            } else {
                skip(parser);
            }
        }
        return new Article(title, content, imageURL, webURL);
    }

    /**
     * A common method for reading text tags
     *
     * @param pParser
     * @param pTag
     * @return String Text Tag
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readTextTag(final XmlPullParser pParser, final String pTag) throws IOException, XmlPullParserException {
        pParser.require(XmlPullParser.START_TAG, NULL_STRING, pTag);
        final String title = readText(pParser);
        pParser.require(XmlPullParser.END_TAG, NULL_STRING, pTag);
        return title;
    }

    /**
     * A common method for reading Image Tag
     *
     * @param pParser
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readImageTag(XmlPullParser pParser) throws IOException, XmlPullParserException {
        String imageLink = null;
        pParser.require(XmlPullParser.START_TAG, NULL_STRING, TAG_ARTICLE_MEDIA);
        String tag = pParser.getName();
        if (tag.equals(TAG_ARTICLE_MEDIA)) {
            imageLink = pParser.getAttributeValue(NULL_STRING, TAG_ARTICLE_IMAGE_URL);
            pParser.nextTag();
        }
        pParser.require(XmlPullParser.END_TAG, NULL_STRING, TAG_ARTICLE_MEDIA);
        return imageLink;
    }

    /**
     * @param pParser
     * @return String
     * @throws IOException
     * @throws XmlPullParserException
     */
    private String readText(XmlPullParser pParser) throws IOException, XmlPullParserException {
        String result = "";
        if (pParser.next() == XmlPullParser.TEXT) {
            result = pParser.getText();
            pParser.nextTag();
        }
        return result;
    }

    /**
     * Skip all the unwanted tags during parsing
     *
     * @param pParser
     * @throws XmlPullParserException
     * @throws IOException
     */
    private void skip(XmlPullParser pParser) throws XmlPullParserException, IOException {
        if (pParser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (pParser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}

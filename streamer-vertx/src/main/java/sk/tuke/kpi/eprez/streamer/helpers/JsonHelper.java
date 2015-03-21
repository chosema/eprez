package sk.tuke.kpi.eprez.streamer.helpers;

import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.StringWriter;
import java.io.Writer;

import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpHeaders;
import org.vertx.java.core.http.HttpServerResponse;
import org.vertx.java.core.json.JsonObject;

import com.allanbank.mongodb.LambdaCallback;
import com.allanbank.mongodb.MongoIterator;
import com.allanbank.mongodb.bson.Document;
import com.allanbank.mongodb.bson.DocumentAssignable;
import com.allanbank.mongodb.bson.element.BinaryElement;
import com.allanbank.mongodb.bson.element.StringElement;
import com.allanbank.mongodb.error.JsonException;

public class JsonHelper {

	public static LambdaCallback<Document> documentWriter(final HttpServerResponse response) {
		return (LambdaCallback<Document>) (thrown, result) -> {
			if (thrown == null) {
				write(response, result);
			} else {
				write(response, new JsonObject().putString("error", thrown.getClass().getName()).encode());
			}
		};
	}

	public static LambdaCallback<Document> dataWriter(final HttpServerResponse response) {
		return (LambdaCallback<Document>) (thrown, result) -> {
			if (thrown == null) {
				final String contentType = result.get(StringElement.class, "contentType").getValue();
				final byte[] content = result.get(BinaryElement.class, "content").getValue();
				response
					.putHeader(HttpHeaders.CONTENT_TYPE, contentType)
					.putHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.length))
					.write(new Buffer(content)).end();
			} else {
				write(response, new JsonObject().putString("error", thrown.getClass().getName()).encode());
			}
		};
	}

	public static void write(final HttpServerResponse response, final DocumentAssignable document) {
		write(response, serialize(document));
	}

	public static void write(final HttpServerResponse response, final MongoIterator<Document> documents) {
		write(response, serialize(documents));
	}

	public static void write(final HttpServerResponse response, final String json) {
		write(response, json, HttpResponseStatus.OK.code());
	}

	public static void write(final HttpServerResponse response, final String json, final int statusCode) {
		//@formatter:off
		response.setStatusCode(statusCode)
			.putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
			.putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
			.end(json);
		//@formatter:on
	}

	public static String serialize(final MongoIterator<Document> data) {
		final StringBuilder result = new StringBuilder();
		int count = 0;
		while (data.hasNext()) {
			result.append(serialize(data.next()));
			if (data.hasNext()) {
				result.append(",");
			}
			count++;
		}
		return count > 1 ? ("[" + result.toString() + "]") : result.toString();
	}

	public static String serialize(final DocumentAssignable document) throws JsonException {
		return serialize(document, new StringWriter()).toString();
	}

	public static Writer serialize(final DocumentAssignable document, final Writer sink) throws JsonException {
		document.asDocument().accept(new JsonSerializationVisitor(sink, true));
		return sink;
	}
}

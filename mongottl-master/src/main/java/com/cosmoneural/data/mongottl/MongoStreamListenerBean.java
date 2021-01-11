package com.cosmoneural.data.mongottl;

import com.mongodb.client.MongoChangeStreamCursor;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.FullDocument;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

@Component
public class MongoStreamListenerBean implements InitializingBean {

	@Resource
	MongoDatabaseFactory mongoDatabaseFactory;

	@Value("${com.tatadigital.product.mongo.dbname}")
	String dbName;

	@Override
	public void afterPropertiesSet() throws Exception {

		Runnable run = () -> { // lambda expression
			MongoDatabase db = mongoDatabaseFactory.getMongoDatabase(dbName);
			MongoCollection<Document> collection = db.getCollection(Offer.class.getSimpleName().toLowerCase());

			// comment next if block and @Indexed property in Offer.java as well for getting
			// the error : Expected String but getting int
			if (!collection.listIndexes().iterator().hasNext()) {
				System.out.println("Creating Index");
				IndexOptions options = new IndexOptions();
				options.expireAfter(10L, TimeUnit.SECONDS);
				collection.createIndex(new Document("createDate", 1), options);
			}

			Bson match = Aggregates.match(Filters.in("operationType", Arrays.asList("delete", "replace", "insert")));
			Bson project = Aggregates.project(fields(include("_id", "ns", "documentKey", "fullDocument")));
			List<Bson> pipeline = Arrays.asList(match, project);

			MongoChangeStreamCursor<ChangeStreamDocument<Document>> cursor = db.watch(pipeline)
					.fullDocument(FullDocument.DEFAULT).cursor();
			while (cursor.hasNext()) {
				// There you go, we got the change document.
				ChangeStreamDocument<Document> csDoc = cursor.next();

				// Let is pick the token which will help us resuming
				// You can save this token in any persistent storage and retrieve it later
				BsonDocument resumeToken = csDoc.getResumeToken();
				// Printing the token
				System.out.println(resumeToken);
				System.out.println(csDoc.getDocumentKey());
				// Printing the document.
				System.out.println(csDoc.getFullDocument());
				// This break is intentional but in real project feel free to remove it.
			}
		};

		new Thread(run).start();
	}
}

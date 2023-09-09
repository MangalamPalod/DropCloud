Hello!!

Solution Description:
Database: Local
Code Flow: 
    a. Uploading a file: It is important that we upload the file in chunk rather than pushing it in one go, as it would be a heavy operation.  
                         So basically we need to divide it in chunks and push it to Kafka topic. We can have a listener in the same repo or different depending on the resources and scalability factor.
                         Now, the listener for the topic, each event will contain information about the chunk and file it belongs and hashValue. We will starting writing this at DB/local.
                         Now, if for some reason we are not able to save even after retries(Database is down, etc), we will have to mark the upload as failed and render on the UI to retry in sometime.
    b. Update a file:    We will divide the file in chunks and push it to kafka topic. When we listen to this, we will be querying the Db for the value of Hash for that particular chunk. 
                         Read is easier than write, it's better to compare chunks hashes for this. Before we start comparing we need to make a replica of the chunk, so in case if the something fails, we have a backup state. 
                         If, the value of the hashes are different, we write over the previous value. If the overwrite somehow fails(I/O exception etc), then we mark the job as failed, mark our replicate state as original and render on UI to retry in sometime. 
                         And once, we get the final chunk for a file(a field in the event marking the EOF chunk), we delete all the subsequent chunks in our DB.
                         This may seem an anti-pattern at first, but overwriting the complete file would be a much heavier operation than this at scale.
    c. Delete/Get file:  This is relatively simpler operation. We will simply query our DB for this.


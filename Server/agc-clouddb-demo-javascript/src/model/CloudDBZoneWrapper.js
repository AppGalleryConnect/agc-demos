const agconnect = require('@agconnect/common-server');
const clouddb = require('@agconnect/database-server/dist/index.js');
const BookInfo = require('./BookInfo.js');

module.exports = class CloudDBZoneWrapper {
    constructor() {
        try {
            /*
            * To Integrate the server sdk, a credential file should be used.
            * Change the value of 'credentialPath' to the path of the credential file.
            * */
            const credentialPath = "resource\\agc-apiclient-xxxx.json";
            agconnect.AGCClient.initialize(agconnect.CredentialParser.toCredential(credentialPath));
            const agcClient = agconnect.AGCClient.getInstance();
            clouddb.AGConnectCloudDB.initialize(agcClient);
            const zoneName = 'QuickStartDemo';
            const cloudDBZoneConfig = new clouddb.CloudDBZoneConfig(zoneName);
            this.cloudDBZoneClient = clouddb.AGConnectCloudDB.getInstance().openCloudDBZone(cloudDBZoneConfig);
        } catch (err) {
            console.log(err);
        }
    }

    async upsertBookInfos(BookInfo) {
        if (!this.cloudDBZoneClient) {
            console.log("CloudDBClient is null, try re-initialize it");
            return;
        }
        try {
            const resp = await this.cloudDBZoneClient.executeUpsert(BookInfo);
            console.log('The number of upsert books is:', resp);
        } catch (error) {
            console.warn('upsertBookInfo=>', error);
        }
    }

    async deleteBookInfos(BookInfo) {
        if (!this.cloudDBZoneClient) {
            console.log("CloudDBClient is null, try re-initialize it");
            return;
        }
        try {
            const resp = await this.cloudDBZoneClient.executeDelete(BookInfo);
            console.log('The number of delete books is:', resp);
        } catch (error) {
            console.warn('deleteBookInfos=>', error);
        }
    }

    async deleteAllBooks() {
        if (!this.cloudDBZoneClient) {
            console.log("CloudDBClient is null, try re-initialize it");
            return;
        }
        try {
            const resp = await this.cloudDBZoneClient.executeDeleteAll(BookInfo.BookInfo);
            console.log('The number of delete books is:', resp);
        } catch (error) {
            console.warn('deleteAllBookInfos=>', error);
        }
    }

    async queryAllBooks() {
        if (!this.cloudDBZoneClient) {
            console.log("CloudDBClient is null, try re-initialize it");
            return;
        }
        try {
            const resp = await this.cloudDBZoneClient.executeQuery(clouddb.CloudDBZoneQuery.where(BookInfo.BookInfo));
            console.log('The number of query books is:', resp.getSnapshotObjects().length);
            this.processQueryResult(resp.getSnapshotObjects());
        } catch (error) {
            console.warn('queryAllBooks=>', error);
        }
    }

    async queryBooks(cloudDBZoneQuery) {
        if (!this.cloudDBZoneClient) {
            console.log("CloudDBClient is null, try re-initialize it");
            return;
        }
        try {
            const resp = await this.cloudDBZoneClient.executeQuery(cloudDBZoneQuery);
            console.log('The number of spc query books is:', resp.getSnapshotObjects().length);
            this.processQueryResult(resp.getSnapshotObjects());
        } catch (error) {
            console.warn('queryBooks=>', error);
        }
    }

    async queryBooksWithOrder() {
        if (!this.cloudDBZoneClient) {
            console.log("CloudDBClient is null, try re-initialize it");
            return;
        }
        try {
            const cloudDBZoneQuery = clouddb.CloudDBZoneQuery.where(BookInfo.BookInfo).orderByDesc("price").limit(3);
            const resp = await this.cloudDBZoneClient.executeQuery(cloudDBZoneQuery);
            console.log('The number of query books is:', resp.getSnapshotObjects().length);
            this.processQueryResult(resp.getSnapshotObjects());
        } catch (error) {
            console.warn('queryBooks=>', error);
        }
    }

    async startAtQuery() {
        let book = new BookInfo.BookInfo();
        book.setId(5);
        book.setBookName("The Red And Black");
        book.setPrice(10.99);
        try {
            const cloudDBZoneQuery = clouddb.CloudDBZoneQuery.where(BookInfo.BookInfo)
                .orderByAsc("bookName").orderByAsc("price").startAt(book);
            const resp = await this.cloudDBZoneClient.executeQuery(cloudDBZoneQuery);
            console.log('The number of query books is:', resp.getSnapshotObjects().length);
            this.processQueryResult(resp.getSnapshotObjects());
        } catch (error) {
            console.warn('queryBooks=>', error);
        }
    }

    async averageQuery() {
        try {
            const cloudDBZoneQuery = clouddb.CloudDBZoneQuery.where(BookInfo.BookInfo);
            const resp = await this.cloudDBZoneClient.executeAverageQuery(cloudDBZoneQuery, 'price');
            console.log('The average price of all books is:', resp);
        } catch (error) {
            console.warn('queryBooks=>', error);
        }
    }

    processQueryResult(bookInfos) {
        for (let i = 0; i < bookInfos.length; i++) {
            const log = "ID:" +  bookInfos[i].getId() + ", Book Name:" + bookInfos[i].getBookName()
                + ", Author:" + bookInfos[i].getAuthor() + ", Publisher:" + bookInfos[i].getAuthor()
                + ", PublishTime:" + bookInfos[i].getPublishTime() + ", Price:" + bookInfos[i].getPrice() + "\n";
            console.log(log);
        }
    }

    async deleteOverdueBooks(cloudDBZoneQuery) {
        try {
            const res = await this.cloudDBZoneClient.runTransaction({
                apply: async (transaction) => {
                    return new Promise(async (resolve, _) => {
                        await transaction.executeQuery(cloudDBZoneQuery).then((data) => {
                            console.log('query data num:' + data.length);
                            transaction.executeDelete(data);
                        }).catch((err) => {
                            console.error(err);
                            resolve(false);
                        });
                        resolve(true);
                    })
                }
            })
            console.log("the transaction result:", res);
        } catch (e) {
            console.error(e);
        }
    }

    getSingleBook() {
        let bookInfo = new BookInfo.BookInfo();
        bookInfo.setId(3);
        bookInfo.setBookName("Les Fleurs du mal");
        bookInfo.setAuthor("Charles Pierre Baudelaire");
        bookInfo.setPublisher("Auguste Poulet-Malassis");
        bookInfo.setPublishTime(new Date('1857-01-01T03:24:00'));
        bookInfo.setPrice(30.99);
        return bookInfo;
    }

    getBooksList() {
        let bookInfo1 = new BookInfo.BookInfo();
        bookInfo1.setId(1);
        bookInfo1.setBookName("Harry Potter1");
        bookInfo1.setAuthor("J. K. Rowling");
        bookInfo1.setPublisher("Bloomsbury Publishing (UK)");
        bookInfo1.setPublishTime(new Date('1997-07-26T03:24:00'));
        bookInfo1.setPrice(80.99);

        let bookInfo2 = new BookInfo.BookInfo();
        bookInfo2.setId(2);
        bookInfo2.setBookName("Murder on the Orient Express");
        bookInfo2.setAuthor("Agatha Christie");
        bookInfo2.setPublisher("Collins Crime Club");
        bookInfo2.setPublishTime(new Date('1934-01-01T03:24:00'));
        bookInfo2.setPrice(50.99);

        let bookInfo3 = new BookInfo.BookInfo();
        bookInfo3.setId(3);
        bookInfo3.setBookName("Les Fleurs du mal");
        bookInfo3.setAuthor("Charles Pierre Baudelaire");
        bookInfo3.setPublisher("Auguste Poulet-Malassis");
        bookInfo3.setPublishTime(new Date('1857-01-01T03:24:00'));
        bookInfo3.setPrice(30.99);

        let bookInfo4 = new BookInfo.BookInfo();
        bookInfo4.setId(4);
        bookInfo4.setBookName("The Moon and Sixpence");
        bookInfo4.setAuthor("William Somerset Maugham");
        bookInfo4.setPublisher("Heinemann UK");
        bookInfo4.setPublishTime(new Date("1919-04-15T03:24:00"));
        bookInfo4.setPrice(40.99);

        let bookInfo5 = new BookInfo.BookInfo();
        bookInfo5.setId(5);
        bookInfo5.setBookName("The Red And Black");
        bookInfo5.setAuthor("Stendhal");
        bookInfo5.setPublisher("A. Levasseur");
        bookInfo5.setPublishTime(new Date("1830-11-01T03:24:00"));
        bookInfo5.setPrice(10.99);

        const bookInfoList = [];
        bookInfoList.push(bookInfo1);
        bookInfoList.push(bookInfo2);
        bookInfoList.push(bookInfo3);
        bookInfoList.push(bookInfo4);
        bookInfoList.push(bookInfo5);

        return bookInfoList;
    }
}
const clouddb = require('@agconnect/database-server/dist/index.js');
const CloudDBZoneWrapper = require("./model/CloudDBZoneWrapper.js");
const BookInfo = require("./model/BookInfo.js");

async function start() {
    const cloudDBZoneWrapper = new CloudDBZoneWrapper();

    const bookInfoList = cloudDBZoneWrapper.getBooksList();

    const book = cloudDBZoneWrapper.getSingleBook();

    // delete all books
    await cloudDBZoneWrapper.deleteAllBooks();

    // upsert one book
    await cloudDBZoneWrapper.upsertBookInfos(book);

    // upsert a list of books
    await cloudDBZoneWrapper.upsertBookInfos(bookInfoList);

    // query all books
    await cloudDBZoneWrapper.queryAllBooks();

    // query books which price less than 50.00
    try {
        const cloudDBZoneQuery = clouddb.CloudDBZoneQuery.where(BookInfo.BookInfo).lessThan("price", 50.00);
        await cloudDBZoneWrapper.queryBooks(cloudDBZoneQuery);
    } catch (err) {
        console.log(err);
    }

    // query Books top 3 expensive books.
    await cloudDBZoneWrapper.queryBooksWithOrder();

    // pagination query: get books before record "The Red And Black", order by book name and price.
    await cloudDBZoneWrapper.startAtQuery();

    // get the average price of all books.
    await cloudDBZoneWrapper.averageQuery();

    // use transaction to delete books which are published earlier than 1900.
    try {
        const cloudDBZoneQuery = await clouddb.CloudDBZoneQuery.where(BookInfo.BookInfo).greaterThanOrEqualTo("publishTime", new Date("1900-01-01T03:24:00"));
        await cloudDBZoneWrapper.deleteOverdueBooks(cloudDBZoneQuery);
    } catch (err) {
        console.log(err);
    }

    // delete one record.
    await cloudDBZoneWrapper.deleteBookInfos(book);

    // delete list record.
    await cloudDBZoneWrapper.deleteBookInfos(bookInfoList);
}

start();

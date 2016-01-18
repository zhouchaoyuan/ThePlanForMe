#Sqlite

###架构（Schema）与契约类（Contract）
架构表示数据库如何组织的正式声明，它体现于您用于创建数据库的`SQL`语句，它有助于创建伴随类，即契约类，契约类是用于定义`URI`、表格和列名称的常数的容器。契约类允许您跨同一软件包中的所有其他类使用相同的常数。 您可以在一个位置更改列名称并使其在您整个代码中传播。以下是一个契约类：

```java

	package cn.zhouchaoyuan.dao;
	import android.provider.BaseColumns;
	public final class BookReaderContract {
    	/*不经意实例化的时候可以条用此构造器*/
    	public BookReaderContract() {}
    	/*实现BaseColumns接口，继承已有主键字段_ID*/
    	public static abstract class BookEntry implements BaseColumns {
        	public static final String TABLE_NAME = "Book";
        	public static final String COLUMN_NAME_AUHOR = "author";
        	public static final String COLUMN_NAME_NAME = "name";
        	public static final String COLUMN_NAME_PRICE = "price";
        	public static final String COLUMN_NAME_PAGES = "pages";
    	}
	}

```

典型的创建数据的语句实例：

```java

	package cn.zhouchaoyuan.dao;
	import static cn.zhouchaoyuan.dao.BookReaderContract.BookEntry.*;
	public class SQLClause {
    	/*静态导入*/
    	public static final String REAL_TYPE = "real";
    	public static final String TEXT_TYPE = " TEXT";
    	public static final String COMMA_SEP = ",";
    	public static final String INTEGER_TYPE = "integer";
    	public static final String SQL_CREATE_ENTRIES =
    	        "CREATE TABLE " + TABLE_NAME + " (" +
    	                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT" +
    	                COLUMN_NAME_AUHOR + TEXT_TYPE + COMMA_SEP +
    	                COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
    	                COLUMN_NAME_PRICE + REAL_TYPE + COMMA_SEP +
    	                COLUMN_NAME_PAGES + INTEGER_TYPE + COMMA_SEP +
    	        " )";
    	public static final String SQL_DELETE_ENTRIES =
        	    "DROP TABLE IF EXISTS " + TABLE_NAME;
	}

```

就像您在设备的内部存储中保存文件那样，`Android`将您的数据库保存在私人磁盘空间，即关联的应用。您的数据是安全的，因为在默认情况下，其他应用无法访问此区域。

###使用SQLiteOpenHelper创建DB

为了管理`Android`里面的数据库，系统提供我们一个`SQLiteOpenHelper`，这是一个抽象类，我们需要自己实现子类并且重写抽象方法`public void onCreate(SQLiteDatabase db)`和`public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)`。然后通过`getReadableDatabase()`或者`getWritableDatabase()`获得一个`SQLiteDatabase`，当然如果存在了要创建的数据库名字，那么`public void onCreate(SQLiteDatabase db)`不会执行。实例如下：

```java

	package cn.zhouchaoyuan.dao;
	import android.content.Context;
	import android.database.sqlite.SQLiteDatabase;
	import android.database.sqlite.SQLiteOpenHelper;
	import static cn.zhouchaoyuan.dao.SQLClause.*;
	public class BookReaderHelper extends SQLiteOpenHelper{
    	/*静态导入*/
   	 private  Context context;
    	public BookReaderHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
    	    super(context, name, factory, version);
    	    this.context = context;
    	}

    	@Override
    	public void onCreate(SQLiteDatabase db) {
    	    db.execSQL(SQL_CREATE_ENTRIES);
    	}

    	@Override
    	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	    //当version版本不同时，会回调这个方法
    	    db.execSQL(SQL_DELETE_ENTRIES);
    	    onCreate(db);
    	}
	}

```

调用下面的语句，`onCreate`被执行（`BookStore.db`还不存在）：

```java

	BookReaderHelper brHelper = new BookReaderHelper(this,"BookStore.db",null,1);
    brHelper.getReadableDatabase();

```

这个时候我们不知道是否真的创建了数据库，我们来到`DDMS`的`File Explorer`查看`data/data/cn.zhouchaoyuan.firstapplication/databases`确实创建了一个`BookStore.db`。如下图：</br>![databases](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W7/databases.png)</br>另外我们可以通过`adb shell`操作数据库，将`C:\Users\chaoyuan\AppData\Local\Android\sdk\platform-tools`(我的电脑环境)配置到环境变量中，在dos中键入`adb shell`，然后进入`data/data/cn.zhouchaoyuan.firstapplication/databases`目录，如下:</br>![shell](https://raw.githubusercontent.com/zhouchaoyuan/ThePlanForMe/master/M3-M4/W7/shell.png)</br>可以看到`sdk\platform-tools`目录下有`sqlite3`的命令，直接在`shell`键入`sqlite3加数据库名字`即可操作指定数据库。

>使用`.table`可以显示所有的表，`.schema`可以显示数据库的建表语句。

因为`onCreate`这个方法只会被执行一次，所以我们可以通过数据库的版本信息来升级数据库，调用`onUpgrade`实现数据库的更新。

###添加数据

添加数据的方法原型：[`public long insert (String table, String nullColumnHack, ContentValues values)`](http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#insert%28java.lang.String,%20java.lang.String,%20android.content.ContentValues%29)

- `table` 要插入表名
- `nullColumnHack` 指定在`ContentValues`为空的情况下框架可在其中插入 NULL 的**列**的名称（如果您将其设置为 "null"， 那么框架将不会在没有值时插入行。） 
- `values` 要插入的数据，以键值对形式保存，键必须和列名相同

根据上面所建的表，编写一个示例插入方法：

```java

	public void insert(SQLiteDatabase db){/*静态导入了常量*/
        ContentValues contentValues = new ContentValues();
        contentValues.put(_ID,1);
        contentValues.put(COLUMN_NAME_AUHOR,AUTHOR);
        contentValues.put(COLUMN_NAME_NAME,"The Plan B");
        contentValues.put(COLUMN_NAME_PAGES,120);
        contentValues.put(COLUMN_NAME_PRICE,23.33);
        db.insert(TABLE_NAME,null,contentValues);
    }

```

###更新数据

更新数据的方法原型：[`public int update (String table, ContentValues values, String whereClause, String[] whereArgs)`](http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#update%28java.lang.String,%20android.content.ContentValues,%20java.lang.String,%20java.lang.String[]%29)

- `table` 要更新的表名
- `values` 要插入的数据，以键值对形式保存，键必须和列名相同，`null`是允许的
- `whereClause` `where`语句的列名，`null`话会更新所有行
- `whereArgs` `where`语句的列名的值，一次对应

根据上面所建的表，编写一个示例更新方法：

```java

	public void update(SQLiteDatabase db){/*静态导入了常量*/
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_PRICE,6.666);
        db.update(TABLE_NAME,contentValues,"_ID = ?",new String[]{"1"});
    }

```

###删除数据

和查询信息一样，删除数据同样需要提供一些删除标准。DB的API提供了一个防止SQL注入的机制来创建查询与删除标准。删除数据的方法原型：[`public int delete (String table, String whereClause, String[] whereArgs)`](http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#delete%28java.lang.String,%20java.lang.String,%20java.lang.String[]%29)

- `table` 要更新的表名
- `whereClause` 可选的`where`语句
- `whereArgs` `whereClause`语句中表达式的？占位参数列表

>SQL Injection：(程序员在编写代码时没有对用户输入数据的合法性进行判断，使应用程序存在安全隐患。用户可以提交一段数据库查询代码，根据程序返回的结果，获得某些他想得知的数据，这就是所谓的SQL Injection，即SQL注入)

根据上面所建的表，编写一个示例删除方法：

```java

	public void delete(SQLiteDatabase db){/*静态导入了常量*/
        db.delete(TABLE_NAME, COLUMN_NAME_PAGES + ">= ?", new String[]{"120"});
    }

```

###查询数据

查询数据的方法原型：[`query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy)`](http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html#query%28java.lang.String,%20java.lang.String[],%20java.lang.String,%20java.lang.String[],%20java.lang.String,%20java.lang.String,%20java.lang.String%29)

- `table` 要查询的表名
- `columns` 想要显示的列，若为空则返回所有列，不建议设置为空，如果不是返回所有列
- `selection` `where`子句，声明要返回的行的要求，如果为空则返回表的所有行
- `selectionArgs` `where`子句对应的条件值
- `groupBy` 分组方式，若为空则不分组
- `having` `having`条件，若为空则返回全部（不建议）
- `orderBy` 排序方式，为空则为默认排序方式

```java

	public void query(SQLiteDatabase db){/*静态导入了常量*/
        String[] columns = {
            COLUMN_NAME_NAME,
            COLUMN_NAME_AUHOR
        };
        String sortOrder =
                _ID + " DESC";

        Cursor c = db.query(
                TABLE_NAME,             // The table to query
                columns,                // The columns to return
                null,                   // The columns for the WHERE clause
                null,                   // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );
        if(c.moveToFirst()){
            do {
                String name = c.getString(c.getColumnIndex(COLUMN_NAME_NAME));
                String author = c.getString(c.getColumnIndex(COLUMN_NAME_AUHOR));
                Log.e("book name: ",name);
                Log.e("author: ",author);
                //Toast.makeText(this,"book: "+name+",author: "+author,Toast.LENGTH_SHORT).show();
            }while(c.moveToNext());
        }
    }

```

###直接使用SQL操作数据库

虽然Android提供了非常多的API来实现增删改查，我们也可以通过直接书写几乎是SQL语句的来答案我们的目的，如下：

```java

db.execSQL("insert into Book(_ID,author,name,price,pages) values(?,?,?,?,?)",new String[]{"1207020203","zcy","The Plan C","12.34","233"});
db.execSQL("updata Book set price = ? where name = ?",new String[]{"12.34","The Plan C"});
db.execSQL("delete from Book where pages >= ?",new String[]{"233"});
db.rawQuery("select * from Book",null);

```


还是见[中文教学](http://hukai.me/android-training-course-in-chinese/basics/data-storage/database.html)吧，详细。 或者[官方教学](http://developer.android.com/intl/zh-cn/training/basics/data-storage/databases.html)
package hupuSpider.models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object SlickTables extends {
  val profile = slick.jdbc.PostgresProfile
} with SlickTables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait SlickTables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = tCommenttable.schema ++ tPosttable.schema ++ tTimetable.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table tCommenttable
   *  @param posturl Database column postUrl SqlType(text)
   *  @param commentid Database column commentId SqlType(int8)
   *  @param commentglobalurl Database column commentGlobalUrl SqlType(text), PrimaryKey
   *  @param commentfloor Database column commentFloor SqlType(int8)
   *  @param commentcontent Database column commentContent SqlType(text)
   *  @param commenterid Database column commenterId SqlType(text)
   *  @param commentername Database column commenterName SqlType(text)
   *  @param lights Database column lights SqlType(int8)
   *  @param replyfloor Database column replyFloor SqlType(int8)
   *  @param boardname Database column boardName SqlType(text)
   *  @param areaname Database column areaName SqlType(text)
   *  @param postid Database column postid SqlType(int8), Default(None) */
  final case class rCommenttable(posturl: String, commentid: Long, commentglobalurl: String, commentfloor: Long, commentcontent: String, commenterid: String, commentername: String, lights: Long, replyfloor: Long, boardname: String, areaname: String, postid: Option[Long] = None)
  /** GetResult implicit for fetching rCommenttable objects using plain SQL queries */
  implicit def GetResultrCommenttable(implicit e0: GR[String], e1: GR[Long], e2: GR[Option[Long]]): GR[rCommenttable] = GR{
    prs => import prs._
    rCommenttable.tupled((<<[String], <<[Long], <<[String], <<[Long], <<[String], <<[String], <<[String], <<[Long], <<[Long], <<[String], <<[String], <<?[Long]))
  }
  /** Table description of table commentTable. Objects of this class serve as prototypes for rows in queries. */
  class tCommenttable(_tableTag: Tag) extends profile.api.Table[rCommenttable](_tableTag, "commentTable") {
    def * = (posturl, commentid, commentglobalurl, commentfloor, commentcontent, commenterid, commentername, lights, replyfloor, boardname, areaname, postid) <> (rCommenttable.tupled, rCommenttable.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(posturl), Rep.Some(commentid), Rep.Some(commentglobalurl), Rep.Some(commentfloor), Rep.Some(commentcontent), Rep.Some(commenterid), Rep.Some(commentername), Rep.Some(lights), Rep.Some(replyfloor), Rep.Some(boardname), Rep.Some(areaname), postid).shaped.<>({r=>import r._; _1.map(_=> rCommenttable.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column postUrl SqlType(text) */
    val posturl: Rep[String] = column[String]("postUrl")
    /** Database column commentId SqlType(int8) */
    val commentid: Rep[Long] = column[Long]("commentId")
    /** Database column commentGlobalUrl SqlType(text), PrimaryKey */
    val commentglobalurl: Rep[String] = column[String]("commentGlobalUrl", O.PrimaryKey)
    /** Database column commentFloor SqlType(int8) */
    val commentfloor: Rep[Long] = column[Long]("commentFloor")
    /** Database column commentContent SqlType(text) */
    val commentcontent: Rep[String] = column[String]("commentContent")
    /** Database column commenterId SqlType(text) */
    val commenterid: Rep[String] = column[String]("commenterId")
    /** Database column commenterName SqlType(text) */
    val commentername: Rep[String] = column[String]("commenterName")
    /** Database column lights SqlType(int8) */
    val lights: Rep[Long] = column[Long]("lights")
    /** Database column replyFloor SqlType(int8) */
    val replyfloor: Rep[Long] = column[Long]("replyFloor")
    /** Database column boardName SqlType(text) */
    val boardname: Rep[String] = column[String]("boardName")
    /** Database column areaName SqlType(text) */
    val areaname: Rep[String] = column[String]("areaName")
    /** Database column postid SqlType(int8), Default(None) */
    val postid: Rep[Option[Long]] = column[Option[Long]]("postid", O.Default(None))
  }
  /** Collection-like TableQuery object for table tCommenttable */
  lazy val tCommenttable = new TableQuery(tag => new tCommenttable(tag))

  /** Entity class storing rows of table tPosttable
   *  @param board Database column board SqlType(text)
   *  @param subarea Database column subarea SqlType(text)
   *  @param id Database column id SqlType(int8)
   *  @param posturl Database column postUrl SqlType(text), PrimaryKey
   *  @param posttitle Database column postTitle SqlType(text)
   *  @param authorname Database column authorName SqlType(text)
   *  @param authorurl Database column authorUrl SqlType(text)
   *  @param content Database column content SqlType(text)
   *  @param time Database column time SqlType(text) */
  final case class rPosttable(board: String, subarea: String, id: Long, posturl: String, posttitle: String, authorname: String, authorurl: String, content: String, time: String)
  /** GetResult implicit for fetching rPosttable objects using plain SQL queries */
  implicit def GetResultrPosttable(implicit e0: GR[String], e1: GR[Long]): GR[rPosttable] = GR{
    prs => import prs._
    rPosttable.tupled((<<[String], <<[String], <<[Long], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String]))
  }
  /** Table description of table postTable. Objects of this class serve as prototypes for rows in queries. */
  class tPosttable(_tableTag: Tag) extends profile.api.Table[rPosttable](_tableTag, "postTable") {
    def * = (board, subarea, id, posturl, posttitle, authorname, authorurl, content, time) <> (rPosttable.tupled, rPosttable.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(board), Rep.Some(subarea), Rep.Some(id), Rep.Some(posturl), Rep.Some(posttitle), Rep.Some(authorname), Rep.Some(authorurl), Rep.Some(content), Rep.Some(time)).shaped.<>({r=>import r._; _1.map(_=> rPosttable.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column board SqlType(text) */
    val board: Rep[String] = column[String]("board")
    /** Database column subarea SqlType(text) */
    val subarea: Rep[String] = column[String]("subarea")
    /** Database column id SqlType(int8) */
    val id: Rep[Long] = column[Long]("id")
    /** Database column postUrl SqlType(text), PrimaryKey */
    val posturl: Rep[String] = column[String]("postUrl", O.PrimaryKey)
    /** Database column postTitle SqlType(text) */
    val posttitle: Rep[String] = column[String]("postTitle")
    /** Database column authorName SqlType(text) */
    val authorname: Rep[String] = column[String]("authorName")
    /** Database column authorUrl SqlType(text) */
    val authorurl: Rep[String] = column[String]("authorUrl")
    /** Database column content SqlType(text) */
    val content: Rep[String] = column[String]("content")
    /** Database column time SqlType(text) */
    val time: Rep[String] = column[String]("time")
  }
  /** Collection-like TableQuery object for table tPosttable */
  lazy val tPosttable = new TableQuery(tag => new tPosttable(tag))

  /** Entity class storing rows of table tTimetable
   *  @param board Database column board SqlType(text)
   *  @param subarea Database column subarea SqlType(text)
   *  @param farthest Database column farthest SqlType(text)
   *  @param last Database column last SqlType(text) */
  final case class rTimetable(board: String, subarea: String, farthest: String, last: String)
  /** GetResult implicit for fetching rTimetable objects using plain SQL queries */
  implicit def GetResultrTimetable(implicit e0: GR[String]): GR[rTimetable] = GR{
    prs => import prs._
    rTimetable.tupled((<<[String], <<[String], <<[String], <<[String]))
  }
  /** Table description of table timeTable. Objects of this class serve as prototypes for rows in queries. */
  class tTimetable(_tableTag: Tag) extends profile.api.Table[rTimetable](_tableTag, "timeTable") {
    def * = (board, subarea, farthest, last) <> (rTimetable.tupled, rTimetable.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(board), Rep.Some(subarea), Rep.Some(farthest), Rep.Some(last)).shaped.<>({r=>import r._; _1.map(_=> rTimetable.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column board SqlType(text) */
    val board: Rep[String] = column[String]("board")
    /** Database column subarea SqlType(text) */
    val subarea: Rep[String] = column[String]("subarea")
    /** Database column farthest SqlType(text) */
    val farthest: Rep[String] = column[String]("farthest")
    /** Database column last SqlType(text) */
    val last: Rep[String] = column[String]("last")

    /** Primary key of tTimetable (database name timeTable_pkey) */
    val pk = primaryKey("timeTable_pkey", (subarea, farthest, last))
  }
  /** Collection-like TableQuery object for table tTimetable */
  lazy val tTimetable = new TableQuery(tag => new tTimetable(tag))
}

package icu.wwj.jmh.mysqlx;

import com.mysql.cj.xdevapi.DeleteStatement;
import com.mysql.cj.xdevapi.Expression;
import com.mysql.cj.xdevapi.SelectStatement;
import com.mysql.cj.xdevapi.Session;
import com.mysql.cj.xdevapi.UpdateStatement;
import icu.wwj.jmh.config.BenchmarkParameters;
import icu.wwj.jmh.util.Strings;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.util.concurrent.ThreadLocalRandom;

@State(Scope.Thread)
public class UnpooledReadWriteMySQLXBenchmark {
    
    private Session session;
    
    private final SelectStatement[] reads = new SelectStatement[BenchmarkParameters.TABLES];
    
    private final UpdateStatement[] indexUpdates = new UpdateStatement[BenchmarkParameters.TABLES];
    
    private final UpdateStatement[] nonIndexUpdate = new UpdateStatement[BenchmarkParameters.TABLES];
    
    private final DeleteStatement[] deletes = new DeleteStatement[BenchmarkParameters.TABLES];
    
    @Setup(Level.Trial)
    public void setup() {
        session = MySQLX.getSession();
        for (int i = 0; i < reads.length; i++) {
            reads[i] = session.getDefaultSchema().getTable("sbtest" + (i + 1)).select("c").where("id = ?");
        }
        for (int i = 0; i < indexUpdates.length; i++) {
            indexUpdates[i] = session.getDefaultSchema().getTable("sbtest" + (i + 1)).update().set("k", new Expression("k + 1")).where("id = ?");
        }
        for (int i = 0; i < nonIndexUpdate.length; i++) {
            nonIndexUpdate[i] = session.getDefaultSchema().getTable("sbtest" + (i + 1)).update().where("id = :id");
        }
        for (int i = 0; i < deletes.length; i++) {
            deletes[i] = session.getDefaultSchema().getTable("sbtest" + (i + 1)).delete().where("id = ?");
        }
    }
    
    @Benchmark
    public void benchReadWrite() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        session.startTransaction();
        for (SelectStatement each : reads) {
            each.clearBindings().bind(random.nextInt(BenchmarkParameters.TABLE_SIZE)).execute();
        }
        indexUpdates[random.nextInt(BenchmarkParameters.TABLES)].clearBindings().bind(random.nextInt(BenchmarkParameters.TABLE_SIZE)).execute();
        nonIndexUpdate[random.nextInt(BenchmarkParameters.TABLES)].set("c", Strings.randomString(120)).clearBindings()
                .bind("id", random.nextInt(BenchmarkParameters.TABLE_SIZE)).execute();
        int table = random.nextInt(BenchmarkParameters.TABLES);
        int id = random.nextInt(BenchmarkParameters.TABLE_SIZE);
        deletes[table].clearBindings().bind(id).execute();
        try {
            session.getDefaultSchema().getTable("sbtest" + (table + 1))
                    .insert("id", "k", "c", "pad")
                    .values(id, random.nextInt(Integer.MAX_VALUE), Strings.randomString(120), Strings.randomString(60)).execute();
        } catch (Exception ex) {
            session.rollback();
            return;
        }
        session.commit();
    }
    
    @TearDown(Level.Trial)
    public void tearDown() {
        session.close();
    }
}

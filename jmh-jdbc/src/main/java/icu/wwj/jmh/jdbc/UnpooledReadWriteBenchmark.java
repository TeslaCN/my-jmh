package icu.wwj.jmh.jdbc;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Prepare Statement (ID = 1): 'COMMIT'
 * Prepare Statement (ID = 2): SELECT c FROM sbtest1 WHERE id=?
 * Prepare Statement (ID = 3): UPDATE sbtest3 SET k=k+1 WHERE id=?
 * Prepare Statement (ID = 4): UPDATE sbtest10 SET c=? WHERE id=?
 * Prepare Statement (ID = 5): DELETE FROM sbtest8 WHERE id=?
 * Prepare Statement (ID = 6): INSERT INTO sbtest8 (id, k, c, pad) VALUES (?, ?, ?, ?)
 * <p>
 * Statement: 'BEGIN'
 * Execute Statement: ID = 2
 * Execute Statement: ID = 2
 * Execute Statement: ID = 2
 * Execute Statement: ID = 2
 * Execute Statement: ID = 2
 * Execute Statement: ID = 2
 * Execute Statement: ID = 2
 * Execute Statement: ID = 2
 * Execute Statement: ID = 2
 * Execute Statement: ID = 2
 * Execute Statement: ID = 3
 * Execute Statement: ID = 4
 * Execute Statement: ID = 5
 * Execute Statement: ID = 6
 * Execute Statement: ID = 1
 */
@State(Scope.Group)
public class UnpooledReadWriteBenchmark {
    
    private final ThreadLocalRandom random = ThreadLocalRandom.current();
    
    private final PreparedStatement[] reads = new PreparedStatement[BenchmarkParameters.TABLES];
    
    private final PreparedStatement[] indexUpdates = new PreparedStatement[BenchmarkParameters.TABLES];
    
    private final PreparedStatement[] nonIndexUpdates = new PreparedStatement[BenchmarkParameters.TABLES];
    
    private final PreparedStatement[] deletes = new PreparedStatement[BenchmarkParameters.TABLES];
    
    private final PreparedStatement[] inserts = new PreparedStatement[BenchmarkParameters.TABLES];
    
    private Connection connection;
    
    @Setup(Level.Trial)
    public void setup() throws Exception {
        connection = Jdbcs.getConnection();
        connection.setAutoCommit(false);
        for (int i = 0; i < reads.length; i++) {
            reads[i] = connection.prepareStatement(String.format("select c from sbtest%d where id=?", i + 1));
        }
        for (int i = 0; i < indexUpdates.length; i++) {
            indexUpdates[i] = connection.prepareStatement(String.format("update sbtest%d set k=k+1 where id=?", i + 1));
        }
        for (int i = 0; i < nonIndexUpdates.length; i++) {
            nonIndexUpdates[i] = connection.prepareStatement(String.format("update sbtest%d set c=? where id=?", i + 1));
        }
        for (int i = 0; i < deletes.length; i++) {
            deletes[i] = connection.prepareStatement(String.format("delete from sbtest%d where id=?", i + 1));
        }
        for (int i = 0; i < inserts.length; i++) {
            inserts[i] = connection.prepareStatement(String.format("insert into sbtest%d (id,k,c,pad) values (?,?,?,?)", i + 1));
        }
    }
    
    @Group
    @Benchmark
    public void benchReadWrite() throws Exception {
        for (PreparedStatement each : reads) {
            each.setInt(1, random.nextInt(BenchmarkParameters.TABLE_SIZE));
            each.execute();
        }
        PreparedStatement indexUpdate = indexUpdates[random.nextInt(BenchmarkParameters.TABLES)];
        indexUpdate.setInt(1, random.nextInt(BenchmarkParameters.TABLE_SIZE));
        indexUpdate.execute();
        PreparedStatement nonIndexUpdate = nonIndexUpdates[random.nextInt(BenchmarkParameters.TABLES)];
        nonIndexUpdate.setString(1, Strings.randomString(120));
        nonIndexUpdate.setInt(2, random.nextInt(BenchmarkParameters.TABLE_SIZE));
        nonIndexUpdate.execute();
        int table = random.nextInt(BenchmarkParameters.TABLES);
        int id = random.nextInt(BenchmarkParameters.TABLE_SIZE);
        PreparedStatement delete = deletes[table];
        delete.setInt(1, id);
        delete.execute();
        PreparedStatement insert = inserts[table];
        insert.setInt(1, id);
        insert.setInt(2, random.nextInt(Integer.MAX_VALUE));
        insert.setString(3, Strings.randomString(120));
        insert.setString(4, Strings.randomString(60));
        connection.commit();
    }
    
    @TearDown(Level.Trial)
    public void tearDown() throws Exception {
        for (PreparedStatement each : reads) {
            each.close();
        }
        for (PreparedStatement each : indexUpdates) {
            each.close();
        }
        for (PreparedStatement each : nonIndexUpdates) {
            each.close();
        }
        for (PreparedStatement each : deletes) {
            each.close();
        }
        for (PreparedStatement each : inserts) {
            each.close();
        }
        connection.close();
    }
}

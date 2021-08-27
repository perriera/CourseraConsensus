import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
 
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class NodeTester {

    @InjectMocks
    Transaction transaction = new Transaction(10);

    @Mock
    Node node;

    @Test
    public void testAdd() {

        Set<Transaction> answer = new HashSet<Transaction>();
        boolean[] followees = { true, true };
        node.setFollowees(followees);
        when(node.sendToFollowers()).thenReturn(answer);

        Assert.assertEquals(node.sendToFollowers(), answer);
        verify(node, times(1)).sendToFollowers();
    }
}
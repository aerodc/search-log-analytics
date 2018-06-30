package modules;

import actors.LogWriterActor;
import akka.routing.DefaultResizer;
import akka.routing.RoundRobinPool;
import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;

public class ActorModule extends AbstractModule implements AkkaGuiceSupport {
    @Override
    protected void configure() {
        DefaultResizer resizer = new DefaultResizer(2, 15);
        bindActor(LogWriterActor.class, LogWriterActor.ACTOR_NAME, props -> new RoundRobinPool(3).withResizer(resizer).props(props));
    }
}

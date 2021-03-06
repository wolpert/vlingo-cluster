// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.outbound;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import io.vlingo.actors.Definition;
import io.vlingo.actors.testkit.TestActor;
import io.vlingo.actors.testkit.TestWorld;
import io.vlingo.cluster.model.AbstractClusterTest;
import io.vlingo.cluster.model.application.ClusterApplicationOutboundStream;
import io.vlingo.cluster.model.node.Id;
import io.vlingo.common.message.ByteBufferPool;
import io.vlingo.common.message.RawMessage;

public class ApplicationOutboundStreamTest extends AbstractClusterTest {
  private static final String Message1 = "Message1";

  private MockManagedOutboundChannelProvider channelProvider;
  private Id localNodeId;
  private ByteBufferPool pool;
  private TestActor<ClusterApplicationOutboundStream> outboundStream;
  private TestWorld world;

  @Test
  public void testBroadcast() {
    final ByteBuffer buffer = ByteBuffer.allocate(properties.operationalBufferSize());
    
    final RawMessage rawMessage1 = buildRawMessageBuffer(buffer, Message1);
    
    outboundStream.actor().broadcast(rawMessage1);
    
    for (final ManagedOutboundChannel channel : allTargetChannels()) {
      assertEquals(Message1, mock(channel).writes.get(0));
    }
  }

  @Test
  public void testSendTo() {
    final Id targetId = Id.of(3);
    
    final ByteBuffer buffer = ByteBuffer.allocate(properties.operationalBufferSize());
    
    final RawMessage rawMessage1 = buildRawMessageBuffer(buffer, Message1);
    
    outboundStream.actor().sendTo(rawMessage1, targetId);
    
    assertEquals(Message1, mock(channelProvider.channelFor(targetId)).writes.get(0));
    
    final Id anotherTargetId = Id.of(2);
    
    outboundStream.actor().sendTo(rawMessage1, anotherTargetId);
    
    assertEquals(Message1, mock(channelProvider.channelFor(anotherTargetId)).writes.get(0));
  }
  
  @Before
  public void setUp() throws Exception {
    super.setUp();
    
    world = TestWorld.start("test-outbound-stream");
    
    localNodeId = Id.of(1);
    
    channelProvider = new MockManagedOutboundChannelProvider(localNodeId, config);
    
    pool = new ByteBufferPool(10, properties.operationalBufferSize());
    
    outboundStream =
            world.actorFor(
                    Definition.has(
                            ApplicationOutboundStreamActor.class,
                            Definition.parameters(channelProvider, pool)),
                    ClusterApplicationOutboundStream.class);
  }
  
  private MockManagedOutboundChannel mock(final ManagedOutboundChannel channel) {
    return (MockManagedOutboundChannel) channel;
  }

  private List<ManagedOutboundChannel> allTargetChannels() {
    return new ArrayList<>(channelProvider.allOtherNodeChannels().values());
  }
}

// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.application;

import java.util.Collection;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Startable;
import io.vlingo.actors.Stoppable;
import io.vlingo.actors.World;
import io.vlingo.cluster.model.Properties;
import io.vlingo.cluster.model.node.Id;
import io.vlingo.cluster.model.node.Node;
import io.vlingo.common.message.RawMessage;

public interface ClusterApplication extends Startable, Stoppable {
  public static ClusterApplication instance(final World world, final Node node) {
    final Class<? extends Actor> clusterApplicationActor =
            Properties.instance.clusterApplicationClass();
    
    return world.actorFor(Definition.has(
            clusterApplicationActor, Definition.parameters(node), "cluster-application"),
            ClusterApplication.class);
  }

  void handleApplicationMessage(final RawMessage message, final ClusterApplicationOutboundStream responder);
  void informAllLiveNodes(final Collection<Id> liveNodes, final boolean isHealthyCluster);
  void informLeaderElected(final Id leaderId, final boolean isHealthyCluster, final boolean isLocalNodeLeading);
  void informLeaderLost(final Id lostLeaderId, final boolean isHealthyCluster);
  void informLocalNodeShutDown(final Id nodeId);
  void informLocalNodeStarted(final Id nodeId);
  void informNodeIsHealthy(final Id nodeId, final boolean isHealthyCluster);
  void informNodeJoinedCluster(final Id nodeId, final boolean isHealthyCluster);
  void informNodeLeftCluster(final Id nodeId, final boolean isHealthyCluster);
  void informQuorumAchieved();
  void informQuorumLost();
}

// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.node;

import java.util.Collection;
import java.util.Set;

public interface Registry {
  void cleanTimedOutNodes();
  void confirmAllLiveNodesByLeader();
  boolean isConfirmedByLeader(final Id id);
  Node currentLeader();
  void declareLeaderAs(final Id id);
  void demoteLeaderOf(final Id id);
  boolean isLeader(final Id id);
  boolean hasLeader();
  Set<Node> liveNodes();
  boolean hasMember(final Id id);
  boolean hasQuorum();
  void join(Node node);
  void leave(final Id id);
  void mergeAllDirectoryEntries(final Collection<Node> nodes);
  void promoteElectedLeader(final Id leaderNodeId);
  void registerRegistryInterest(final RegistryInterest interest);
  void updateLastHealthIndication(final Id id);
}

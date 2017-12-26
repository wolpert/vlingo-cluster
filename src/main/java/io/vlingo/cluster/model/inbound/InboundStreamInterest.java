// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.inbound;

import io.vlingo.cluster.model.node.AddressType;
import io.vlingo.common.message.RawMessage;

public interface InboundStreamInterest {
  void handleInboundStreamMessage(final AddressType addressType, final RawMessage message, final InboundResponder responder);
}
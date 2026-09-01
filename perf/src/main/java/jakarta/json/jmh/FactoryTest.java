/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */
package jakarta.json.jmh;

import jakarta.json.Json;
import jakarta.json.JsonBuilderFactory;
import jakarta.json.JsonNumber;
import jakarta.json.JsonString;
import jakarta.json.spi.JsonProvider;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

/**
 * Benchmarks for issue #154 — "Json factory methods are very inefficient".
 *
 * <p>Each {@code Json.*} convenience method calls {@link JsonProvider#provider()}
 * on every invocation, which triggers a ServiceLoader scan and is orders of
 * magnitude slower than caching the provider or using a factory.
 *
 * <p>Three strategies are compared for each operation:
 * <ol>
 *   <li><b>viaJson</b>       – {@link Json} helper (slow path, provider lookup per call)</li>
 *   <li><b>viaFactory</b>    – cached {@link JsonBuilderFactory} (recommended workaround for builders)</li>
 *   <li><b>viaProvider</b>   – cached static {@link JsonProvider} (recommended workaround for createValue)</li>
 * </ol>
 *
 * @see <a href="https://github.com/jakartaee/jsonp-api/issues/154">jakartaee/jsonp-api#154</a>
 */
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class FactoryTest {

    // --- cached instances (the recommended workarounds from issue #154) ---

    private static final JsonProvider PROVIDER = JsonProvider.provider();

    private static final JsonBuilderFactory BUILDER_FACTORY = Json.createBuilderFactory(null);

    // -----------------------------------------------------------------------
    // createObjectBuilder
    // -----------------------------------------------------------------------

    @Benchmark
    public void createObjectBuilder_viaJson(Blackhole bh) {
        bh.consume(Json.createObjectBuilder());
    }

    @Benchmark
    public void createObjectBuilder_viaFactory(Blackhole bh) {
        bh.consume(BUILDER_FACTORY.createObjectBuilder());
    }

    @Benchmark
    public void createObjectBuilder_viaProvider(Blackhole bh) {
        bh.consume(PROVIDER.createObjectBuilder());
    }

    // -----------------------------------------------------------------------
    // createArrayBuilder
    // -----------------------------------------------------------------------

    @Benchmark
    public void createArrayBuilder_viaJson(Blackhole bh) {
        bh.consume(Json.createArrayBuilder());
    }

    @Benchmark
    public void createArrayBuilder_viaFactory(Blackhole bh) {
        bh.consume(BUILDER_FACTORY.createArrayBuilder());
    }

    @Benchmark
    public void createArrayBuilder_viaProvider(Blackhole bh) {
        bh.consume(PROVIDER.createArrayBuilder());
    }

    // -----------------------------------------------------------------------
    // createValue (String) — no JsonBuilderFactory equivalent
    // -----------------------------------------------------------------------

    @Benchmark
    public void createValueString_viaJson(Blackhole bh) {
        bh.consume(Json.createValue("test"));
    }

    @Benchmark
    public void createValueString_viaProvider(Blackhole bh) {
        bh.consume(PROVIDER.createValue("test"));
    }

    // -----------------------------------------------------------------------
    // createValue (int) — no JsonBuilderFactory equivalent
    // -----------------------------------------------------------------------

    @Benchmark
    public void createValueInt_viaJson(Blackhole bh) {
        bh.consume(Json.createValue(42));
    }

    @Benchmark
    public void createValueInt_viaProvider(Blackhole bh) {
        bh.consume(PROVIDER.createValue(42));
    }
}

package at.jku.tk.steinbauer.bigdata.giraph;

import org.apache.giraph.Algorithm;
import org.apache.giraph.edge.Edge;
import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;

@Algorithm(name = "Max In Degree")
public class MaxInDegreeComputation extends BasicComputation<LongWritable, DoubleWritable, FloatWritable, DoubleWritable> {
	@Override
	public void compute(final Vertex<LongWritable, DoubleWritable, FloatWritable> vertex, final Iterable<DoubleWritable> messages) {
		if (getSuperstep() == 0L) {
			final Iterable<Edge<LongWritable, FloatWritable>> edges = vertex.getEdges();
			for (final Edge<LongWritable, FloatWritable> e : edges) {
				sendMessage(e.getTargetVertexId(), new DoubleWritable(1.0));
			}
			// or sendMessageToAllEdges(new LongWritable(1));
		} else {
			long sum = 0;
			for (final DoubleWritable m : messages) {
				sum += m.get();
			}
			final DoubleWritable vertexValue = vertex.getValue();
			vertexValue.set(sum);
			vertex.setValue(vertexValue);
			vertex.voteToHalt();
		}
	}
}

package at.jku.tk.steinbauer.bigdata.giraph;

import java.io.IOException;

import org.apache.giraph.Algorithm;
import org.apache.giraph.edge.Edge;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;

@Algorithm(name = "Max In Degree")
public class MaxInDegreeComputation extends Vertex<LongWritable, DoubleWritable, FloatWritable, DoubleWritable> {

	@Override
	public void compute(Iterable<DoubleWritable> messages) throws IOException {
		if(getSuperstep() == 0L) {
			Iterable<Edge<LongWritable, FloatWritable>> edges = getEdges();
			for(Edge<LongWritable, FloatWritable> e : edges) {
				sendMessage(e.getTargetVertexId(), new DoubleWritable(1.0));
			}
			// or sendMessageToAllEdges(new LongWritable(1));
		}else{
			long sum = 0;
			for(DoubleWritable m : messages) {
				sum += m.get();
			}
			DoubleWritable vertexValue = getValue();
			vertexValue.set(sum);
			setValue(vertexValue);
			voteToHalt();
		}
	}
	
}

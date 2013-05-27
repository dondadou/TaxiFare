package com.android.taxifare.google.apis;

import java.io.Serializable;
import java.util.List;

import com.google.api.client.util.Key;

public class Route implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Key
	private String summary;
	
	@Key
	private List<Leg> legs;
	
	@Key
	private Point overview_polyline;
	
	@Key
	private String copyrights;
	
	public Point getPolyline(){
		return overview_polyline;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getSummary(){
		return summary;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Leg> getLegs(){
		return legs;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getCopyright(){
		return copyrights;
	}
	
	public static class Point implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		@Key
		private String points;
		
		public String getPoints(){
			return points;
		}
	}
	
	/**
	 * 
	 * @author Daniel Olivier
	 *
	 */
	public static class Leg implements Serializable{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Key
		private Distance distance;
		
		@Key
		private Location start_location;
		
		@Key
		private Location end_location;
		
		@Key
		private List<Step> steps;
		
		public Distance getDistance(){
			return distance;
		}
		
		public List<Step> getStep(){
			return steps;
		}
		
		public Location getStartLocation(){
			return start_location;
		}
		
		public Location getEndLocation(){
			return end_location;
		}
		/**
		 * 
		 * @author Daniel Olivier
		 *
		 */
		public static class Distance implements Serializable{
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Key
			private double value;
			
			@Key
			private String text;
			
			/**
			 * 
			 * @return
			 */
			public double getValue(){
				return value;
			}
			
			/**
			 * 
			 * @return
			 */
			public String getText(){
				return text;
			}
		}
	
		public static class Step implements Serializable{
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Key
			private Location start_location;
			
			@Key
			private Location end_location;
			
			public Location getStartLocation(){
				return start_location;
			}
			
			public Location getEndLocation(){
				return end_location;
			}
		}
		
	}

}

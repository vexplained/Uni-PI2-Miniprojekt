package de.grotefober.nbodysim.sim;

import java.io.Serializable;

/**
 * Similar to {@link Vector2D}. Inheritance is not used here as method javadoc as well as method names differ.
 */
public abstract class Vector2D implements Cloneable
{
	/**
	 * The {@code Double} class defines a vector specified in
	 * {@code double} precision.
	 * <br>
	 * Math operations do not modify the initial Vector2D object itself (treated as immutable). Instead, the result of
	 * the operation is returned. This results in larger computation overhead as new instances of vectors are created
	 * for every operation.
	 */
	public static class Double extends Vector2D implements Serializable
	{
		private static final long serialVersionUID = -5110710768719730760L;

		/**
		 * The X coordinate of this {@code Vector2D}.
		 *
		 * @serial
		 */
		protected double x;

		/**
		 * The Y coordinate of this {@code Vector2D}.
		 *
		 * @serial
		 */
		protected double y;

		/**
		 * Variant of {@link Vector2D} with mutable math operations.
		 */
		public static class Mutable extends Vector2D.Double implements Serializable
		{
			private static final long serialVersionUID = -8390146820436311931L;

			/**
			 * Constructs and initializes a {@code Vector2D} with
			 * coordinates (0,&nbsp;0).
			 */
			public Mutable()
			{
				super();
			}

			/**
			 * Constructs and initializes a {@code Vector2D} with the
			 * specified coordinates.
			 *
			 * @param x
			 *            the X coordinate of the newly
			 *            constructed {@code Vector2D}
			 * @param y
			 *            the Y coordinate of the newly
			 *            constructed {@code Vector2D}
			 */
			public Mutable(double x, double y)
			{
				super(x, y);
			}

			/**
			 * Creates a mutable instance of the given vector.
			 */
			public Mutable(Vector2D vector)
			{
				super(vector.getX(), vector.getY());
			}

			/**
			 * Adds the vector <code>addend</code> to this vector.
			 * 
			 * @param addend
			 *            the vector to add to this vector
			 * @return this object
			 */
			@Override
			public Vector2D add(Vector2D addend)
			{
				this.setLocation(this.getX() + addend.getX(), this.getY() + addend.getY());
				return this;
			}

			/**
			 * Subtracts the vector <code>subtrahend</code> from this vector.
			 * 
			 * @param subtrahend
			 *            the vector to subtract from this vector
			 * @return this object
			 */
			@Override
			public Vector2D subtract(Vector2D subtrahend)
			{
				this.setLocation(this.getX() - subtrahend.getX(), this.getY() - subtrahend.getY());
				return this;
			}

			/**
			 * Scales this vector by <code>scalar</code>.
			 * 
			 * @param scalar
			 *            the amount to scale this vector by.
			 * @return this object
			 */
			@Override
			public Vector2D scale(double scalar)
			{
				this.setLocation(this.getX() * scalar, this.getY() * scalar);
				return this;
			}

			@Override
			public Vector2D clamp(double maxLength)
			{
				double lengthSq = this.distanceSq(0, 0);
				double maxLengthSq = maxLength * maxLength;
				if (lengthSq > maxLengthSq)
				{
					double factor = Math.sqrt(maxLengthSq / lengthSq);
					this.setLocation(this.getX() * factor, this.getY() * factor);
					return this;
				}
				return this;
			}

			/**
			 * Normalizes this vector to a length of one.
			 * 
			 * @return this object.
			 */
			@Override
			public Vector2D norm()
			{
				this.scale(1d / this.getMagnitude());
				return this;
			}

			@Override
			public String toString()
			{
				return "Vector2D.Double.Mutable[" + x + ", " + y + "]";
			}
		}

		/**
		 * Constructs and initializes a {@code Vector2D} with
		 * coordinates (0,&nbsp;0).
		 */
		public Double()
		{
		}

		/**
		 * Constructs and initializes a {@code Vector2D} with the
		 * specified coordinates.
		 *
		 * @param x
		 *            the X coordinate of the newly
		 *            constructed {@code Vector2D}
		 * @param y
		 *            the Y coordinate of the newly
		 *            constructed {@code Vector2D}
		 */
		public Double(double x, double y)
		{
			this.x = x;
			this.y = y;
		}

		/**
		 * Creates a copy of the given vector.
		 */
		public Double(Vector2D vector)
		{
			this(vector.getX(), vector.getY());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getX()
		{
			return x;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getY()
		{
			return y;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getMagnitudeSq()
		{
			return Math.sqrt(getMagnitude());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double getMagnitude()
		{
			return this.x * this.x + this.y * this.y;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setLocation(double x, double y)
		{
			this.x = x;
			this.y = y;
		}

		/**
		 * Returns a {@code String} that represents the value
		 * of this {@code Vector2D}.
		 *
		 * @return a string representation of this {@code Vector2D}.
		 */
		@Override
		public String toString()
		{
			return "Vector2D.Double[" + x + ", " + y + "]";
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setX(double x)
		{
			this.x = x;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setY(double y)
		{
			this.y = y;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Vector2D add(Vector2D addend)
		{
			return new Vector2D.Double(this.x + addend.getX(), this.y + addend.getY());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Vector2D subtract(Vector2D subtrahend)
		{
			return new Vector2D.Double(this.x - subtrahend.getX(), this.y - subtrahend.getY());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Vector2D scale(double scalar)
		{
			return new Vector2D.Double(this.x * scalar, this.y * scalar);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public double dot(Vector2D other)
		{
			return this.x * other.getX() + this.y * other.getY();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Vector2D norm()
		{
			return this.scale(1 / this.getMagnitude());
		}
	}

	/**
	 * This is an abstract class that cannot be instantiated directly.
	 * Type-specific implementation subclasses are available for
	 * instantiation and provide a number of formats for storing
	 * the information necessary to satisfy the various accessor
	 * methods below.
	 *
	 * @see java.awt.geom.Vector2D.Double
	 * @see java.awt.Point2D
	 */
	protected Vector2D()
	{
	}

	/**
	 * Returns the X coordinate of this {@code Vector2D} in
	 * {@code double} precision.
	 *
	 * @return the X coordinate of this {@code Vector2D}.
	 */
	public abstract double getX();

	/**
	 * Returns the Y coordinate of this {@code Vector2D} in
	 * {@code double} precision.
	 *
	 * @return the Y coordinate of this {@code Vector2D}.
	 */
	public abstract double getY();

	/**
	 * Sets the X coordinate of this {@code Vector2D} to the
	 * specified {@code double} value.
	 *
	 * @param x
	 *            the new X coordinate of this {@code Vector2D}
	 */
	public abstract void setX(double x);

	/**
	 * Sets the Y coordinate of this {@code Vector2D} to the
	 * specified {@code double} value.
	 *
	 * @param y
	 *            the new Y coordinate of this {@code Vector2D}
	 */
	public abstract void setY(double y);

	/**
	 * Sets the position of this {@code Vector2D} to the
	 * specified {@code double} coordinates.
	 *
	 * @param x
	 *            the new X coordinate of this {@code Vector2D}
	 * @param y
	 *            the new Y coordinate of this {@code Vector2D}
	 */
	public abstract void setLocation(double x, double y);

	/**
	 * Sets the position of this {@code Vector2D} to the same
	 * coordinates as the specified {@code Vector2D} object.
	 *
	 * @param p
	 *            the specified {@code Vector2D} to which to set
	 *            this {@code Vector2D}
	 */
	public void setLocation(Vector2D p)
	{
		setLocation(p.getX(), p.getY());
	}

	/**
	 * Returns the square of the length or magnitude of a vector.
	 *
	 * @param x
	 *            the X coordinate of the vector
	 * @param y
	 *            the Y coordinate of the vector
	 * @return the square of the length of the specified vector.
	 */
	public static double magnitudeSq(double x, double y)
	{
		return (x * x + y * y);
	}

	/**
	 * Returns the length or magnitude of a vector.
	 *
	 * @param x
	 *            the X coordinate of the vector
	 * @param y
	 *            the Y coordinate of the vector
	 * @return the length of the specified vector.
	 */
	public static double magnitude(double x, double y)
	{
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * Returns the square of the length or magnitude of this vector instance.
	 *
	 * @return the square of the length of this vector instance.
	 */
	public abstract double getMagnitudeSq();

	/**
	 * Returns the length or magnitude of this vector instance.
	 *
	 * @return the length of this vector instance.
	 */
	public abstract double getMagnitude();

	/**
	 * Calculates the sum of this vector plus the <code>addend</code>.
	 * <br>
	 * If this instance is not a subclass of {@link Vector2D.Double.Mutable}, this instance is not
	 * modified.
	 * 
	 * @return <code>this + addend</code>
	 */
	public abstract Vector2D add(Vector2D addend);

	/**
	 * Calculates the difference of this vector minus the <code>subtrahend</code>.
	 * <br>
	 * If this instance is not a subclass of {@link Vector2D.Double.Mutable}, this instance is not
	 * modified.
	 * 
	 * @return <code>this - subtrahend</code>
	 */
	public abstract Vector2D subtract(Vector2D subtrahend);

	/**
	 * Calculates the scaled version of this vector.
	 * <br>
	 * If this instance is not a subclass of {@link Vector2D.Double.Mutable}, this instance is not
	 * modified.
	 * 
	 * @return <code>scalar * this</code>
	 */
	public abstract Vector2D scale(double scalar);

	/**
	 * Calculates the dot product of this and <code>other</code> vector. The dot product is commutative.
	 * <br>
	 * If this instance is not a subclass of {@link Vector2D.Double.Mutable}, this instance is not
	 * modified.
	 * 
	 * @return <code>this \u22c5 other</code>
	 */
	public abstract double dot(Vector2D other);

	/**
	 * Calculates the normalized version of this vector.
	 * <br>
	 * If this instance is not a subclass of {@link Vector2D.Double.Mutable}, this instance is not
	 * modified.
	 * 
	 * @return <code>this / |this|</code>
	 */
	public abstract Vector2D norm();

	/**
	 * Returns the square of the distance between two points.
	 *
	 * @param x1
	 *            the X coordinate of the first specified point
	 * @param y1
	 *            the Y coordinate of the first specified point
	 * @param x2
	 *            the X coordinate of the second specified point
	 * @param y2
	 *            the Y coordinate of the second specified point
	 * @return the square of the distance between the two
	 *         sets of specified coordinates.
	 */
	public static double distanceSq(double x1, double y1,
			double x2, double y2)
	{
		x1 -= x2;
		y1 -= y2;
		return (x1 * x1 + y1 * y1);
	}

	/**
	 * Returns the distance between two points.
	 *
	 * @param x1
	 *            the X coordinate of the first specified point
	 * @param y1
	 *            the Y coordinate of the first specified point
	 * @param x2
	 *            the X coordinate of the second specified point
	 * @param y2
	 *            the Y coordinate of the second specified point
	 * @return the distance between the two sets of specified
	 *         coordinates.
	 */
	public static double distance(double x1, double y1,
			double x2, double y2)
	{
		x1 -= x2;
		y1 -= y2;
		return Math.sqrt(x1 * x1 + y1 * y1);
	}

	/**
	 * Returns the square of the distance from this
	 * {@code Vector2D} treated as a point to a specified point.
	 *
	 * @param px
	 *            the X coordinate of the specified point to be measured
	 *            against this {@code Point2D}
	 * @param py
	 *            the Y coordinate of the specified point to be measured
	 *            against this {@code Point2D}
	 * @return the square of the distance between this
	 *         {@code Vector2D} and the specified point.
	 */
	public double distanceSq(double px, double py)
	{
		px -= getX();
		py -= getY();
		return (px * px + py * py);
	}

	/**
	 * Returns the square of the distance from this
	 * {@code Vector2D} treated as a point to a specified {@code Vector2D}.
	 *
	 * @param pt
	 *            the specified point to be measured
	 *            against this {@code Vector2D}
	 * @return the square of the distance between this
	 *         {@code Vector2D} to a specified {@code Vector2D}.
	 */
	public double distanceSq(Vector2D pt)
	{
		double px = pt.getX() - this.getX();
		double py = pt.getY() - this.getY();
		return (px * px + py * py);
	}

	/**
	 * Returns the distance from this {@code Vector2D} to
	 * a specified point.
	 *
	 * @param px
	 *            the X coordinate of the specified point to be measured
	 *            against this {@code Vector2D}
	 * @param py
	 *            the Y coordinate of the specified point to be measured
	 *            against this {@code Vector2D}
	 * @return the distance between this {@code Vector2D}
	 *         and a specified point.
	 */
	public double distance(double px, double py)
	{
		px -= getX();
		py -= getY();
		return Math.sqrt(px * px + py * py);
	}

	/**
	 * Returns the distance from this {@code Vector2D} to a
	 * specified {@code Vector2D}.
	 *
	 * @param pt
	 *            the specified point to be measured
	 *            against this {@code Vector2D}
	 * @return the distance between this {@code Vector2D} and
	 *         the specified {@code Vector2D}.
	 */
	public double distance(Vector2D pt)
	{
		double px = pt.getX() - this.getX();
		double py = pt.getY() - this.getY();
		return Math.sqrt(px * px + py * py);
	}

	public Vector2D clamp(double maxLength)
	{
		double lengthSq = this.distanceSq(0, 0);
		double maxLengthSq = maxLength * maxLength;
		if (lengthSq > maxLengthSq)
		{
			double factor = Math.sqrt(maxLengthSq / lengthSq);
			return this.scale(factor);
		}
		return this;
	}

	/**
	 * Creates a new object of the same class and with the
	 * same contents as this object.
	 *
	 * @return a clone of this instance.
	 * @throws OutOfMemoryError
	 *             if there is not enough memory.
	 * @see java.lang.Cloneable
	 */
	@Override
	public Object clone()
	{
		try
		{
			return super.clone();
		} catch (CloneNotSupportedException e)
		{
			// this shouldn't happen, since we are Cloneable
			throw new InternalError(e);
		}
	}

	/**
	 * Returns the hashcode for this {@code Vector2D}.
	 *
	 * @return a hash code for this {@code Vector2D}.
	 */
	@Override
	public int hashCode()
	{
		long bits = java.lang.Double.doubleToLongBits(getX());
		bits ^= java.lang.Double.doubleToLongBits(getY()) * 31;
		return (((int) bits) ^ ((int) (bits >> 32)));
	}

	/**
	 * Determines whether or not two vectors are equal. Two instances of
	 * {@code Vector2D} are equal if the values of their
	 * {@code x} and {@code y} member fields, representing
	 * their position in the coordinate space, are the same.
	 *
	 * @param obj
	 *            an object to be compared with this {@code Vector2D}
	 * @return {@code true} if the object to be compared is
	 *         an instance of {@code Vector2D} and has
	 *         the same values; {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Vector2D)
		{
			Vector2D v2d = (Vector2D) obj;
			return (getX() == v2d.getX()) && (getY() == v2d.getY());
		}
		return super.equals(obj);
	}

	@Override
	public String toString()
	{
		return "Vector2D[%d, %d]".formatted(getX(), getY());
	}
}

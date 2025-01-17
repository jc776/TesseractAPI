package tesseract.api.gt;

import tesseract.api.IConnectable;
import tesseract.api.IRefreshable;
import tesseract.util.Dir;

/**
 * An electric node is the unit of interaction with electric inventories.
 * <p>
 * Derived from the Redstone Flux power system designed by King Lemming and originally utilized in Thermal Expansion and related mods.
 * Created with consent and permission of King Lemming and Team CoFH. Released with permission under LGPL 2.1 when bundled with Forge.
 * </p>
 */
public interface IGTNode extends IRefreshable {

	/**
	 * Adds energy to the node. Returns quantity of energy that was accepted.
	 * @param maxReceive Maximum amount of energy to be inserted.
	 * @param simulate If true, the insertion will only be simulated.
	 * @return Amount of energy that was (or would have been, if simulated) accepted by the storage.
	 */
	long insert(long maxReceive, boolean simulate);

	/**
	 * Removes energy from the node. Returns quantity of energy that was removed.
	 * @param maxExtract Maximum amount of energy to be extracted.
	 * @param simulate If true, the extraction will only be simulated.
	 * @return Amount of energy that was (or would have been, if simulated) extracted from the storage.
	 */
	long extract(long maxExtract, boolean simulate);

	/**
	 * @return Gets the amount of energy currently stored.
	 */
	long getEnergy();

	/**
	 * @return Gets the maximum amount of energy that can be stored.
	 */
	long getCapacity();

	/**
	 * @return Gets the maximum amount of amperage that can be output.
	 */
	int getOutputAmperage();

	/**
	 * @return Gets the maximum amount of voltage that can be output.
	 */
	int getOutputVoltage();

	/**
	 * @return Gets the maximum amount of amperage that can be input.
	 */
	int getInputAmperage();

	/**
	 * @return Gets the maximum amount of voltage that can be input.
	 */
	int getInputVoltage();

	/**
	 * Gets if this storage can have energy extracted.
	 * @return If this is false, then any calls to extractEnergy will return 0.
	 */
	boolean canOutput();

	/**
	 * Used to determine if this storage can receive energy.
	 * @return If this is false, then any calls to receiveEnergy will return 0.
	 */
	boolean canInput();

	/**
	 * Used to determine if this storage can receive energy in the given direction.
	 * @param direction the direction.
	 * @return If this is false, then any calls to receiveEnergy will return 0.
	 */
	boolean canInput(Dir direction);

	/**
	 * Used to determine which sides can output energy (if any).
	 * @param direction Direction to the output.
	 * @return Returns true if the given direction is output side.
	 */
	boolean canOutput(Dir direction);

	GTConsumer.State getState();
}

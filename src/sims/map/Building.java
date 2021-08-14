package sims.map;

import java.util.LinkedList;
import java.util.Random;

import sims.items.Decoration;
import sims.items.GeneralItem;
import sims.items.Bladder.Toilet;
import sims.items.Energy.Bed;
import sims.items.Energy.Sofa;
import sims.items.Fun.Pool;
import sims.items.Fun.TV;
import sims.items.Hygiene.Shower;
import sims.items.Hygiene.Sink;
import sims.items.Other.Counter;
import sims.items.Other.Fridge;
import sims.items.Other.Microwave;
import sims.items.Other.Table;
import sims.items.Social.PC;
import sims.items.Social.Phone;

public class Building {
	private final int MSZ = 41;
	private final int minSize = 6;
	private Objects[][] home;
	private LinkedList<Room> rooms;
	private LinkedList<Room> smallRooms;
	private LinkedList<GeneralItem> interactions, nonInteractions;

	public Building() {
		this.home = new Objects[MSZ][MSZ];
		rooms = new LinkedList<Room>();
		smallRooms = new LinkedList<Room>();

		// Contains each item that is directly interacted by the sim
		interactions = new LinkedList<GeneralItem>();
		// Does not contain any item which the sim does not interact with directly
		nonInteractions = new LinkedList<GeneralItem>();

		SetupWalls();
		Room house = new Room(0, 0, MSZ, MSZ);
		paintRoom(house, Objects.SPACE1, Objects.SPACE2, true);
		roomGenerator(house);
	}

	public GeneralItem getItem(Objects obID) {
		for (int i = 0; i < nonInteractions.size(); i++) {
			if (nonInteractions.get(i).getObjectID() == obID) {
				return nonInteractions.get(i);
			}
		}
		for (int i = 0; i < interactions.size(); i++) {
			if (interactions.get(i).getObjectID() == obID)
				return interactions.get(i);
		}
		return null;
	}

	public void drawWalls(Room room) {
		int width = room.getWidth();
		int height = room.getHeight();
		int left = room.getLeftWall();
		int upper = room.getUpperWall();

		for (int i = 0; i < width; i++) {
			home[upper][left + i] = Objects.WALL;
		}
		for (int i = 0; i < height; i++) {
			home[upper + i][left] = Objects.WALL;
		}
	}

	void paintRoom(Room room, Objects color1, Objects color2, boolean regular) {
		for (int i = 1; i < room.getHeight() - 1; i++) {
			for (int j = 1; j < room.getWidth() - 1; j++) {
				int x = (i - j) % 2;
				int y = (i - j) & 2;
				if ((x == 0 && regular) || (y == 0 && !regular))
					home[i + room.getUpperWall()][j + room.getLeftWall()] = color1;
				else
					home[i + room.getUpperWall()][j + room.getLeftWall()] = color2;
			}
		}
	}

	public void roomGenerator(Room room) {
		Random rand = new Random();
		int x = rand.nextInt(4);
		final int div = MSZ / 2;

		Room topLeft = new Room(0, 0, div + 1, div + 1);
		Room topRight = new Room(div, 0, div + 1, div + 1);
		Room bottomLeft = new Room(0, div, div + 1, div + 1);
		Room bottomRight = new Room(div, div, div + 1, div + 1);

		Room left, right;

		rooms.add(topLeft);
		rooms.add(topRight);
		rooms.add(bottomLeft);
		rooms.add(bottomRight);

		Room aRoom = rooms.get(x);
		rooms.remove(x);
		left = new Room(aRoom.getLeftWall(), aRoom.getUpperWall(), div / 2 + 1, aRoom.getHeight());
		right = new Room(aRoom.getLeftWall() + div / 2, aRoom.getUpperWall(), div / 2 + 1, aRoom.getHeight());
		smallRooms.add(left);
		smallRooms.add(right);

		for (int i = 0; i < rooms.size(); i++) {
			drawWalls(rooms.get(i));
		}
		for (int i = 0; i < smallRooms.size(); i++) {
			drawWalls(smallRooms.get(i));
		}
		setRooms();
	}

	void setRooms() {
		// Choose the rooms
		Room garden = rooms.get(0);
		Room kitchen = rooms.get(1);
		Room saloon = rooms.get(2);
		Random rand = new Random();
		int x = rand.nextInt(smallRooms.size());
		Room bathroom = smallRooms.get(x);
		Room bedroom = smallRooms.get(smallRooms.size() - 1 - x);

		// Create each object in every room
		setKitchen(kitchen);
		setSaloon(saloon);
		setBathroom(bathroom);
		setBedroom(bedroom);
		setGarden(garden);

		// Draw all objects
		// Items that are not interacted directly are drawn first. (Interactions are
		// above these items)
		for (int i = 0; i < nonInteractions.size(); i++) {
			GeneralItem item = nonInteractions.get(i);
			item.draw(home);
		}

		for (int i = 0; i < interactions.size(); i++) {
			GeneralItem item = interactions.get(i);
			item.draw(home);
		}

		// Doors should be set after each object is already in place, otherwise the door
		// can be blocked..
		setDoors(kitchen);
		setDoors(saloon);
		setDoors(bathroom);
		setDoors(bedroom);
		setDoors(garden);
	}

	void setDoors2(Room room) {
		Random rand = new Random();
		for (int i = 0; i < 2; i++) {
			int x = rand.nextInt(room.getWidth() - 2) + 1; // horizontal
			if (room.getUpperWall() > 0) {
				while (!Objects.isFloor(this.home[room.getUpperWall() + 1][x + room.getLeftWall()])
						|| !Objects.isFloor(this.home[room.getUpperWall() - 1][x + room.getLeftWall()])) {
					x = rand.nextInt(room.getWidth() - 2) + 1;
				}
				this.home[room.getUpperWall()][x + room.getLeftWall()] = Objects.DOOR;
			}
			x = rand.nextInt(room.getHeight() - 2) + 1; // vertical
			if (room.getLeftWall() > 0) {
				while (!Objects.isFloor(this.home[room.getUpperWall() + x][room.getLeftWall() + 1])
						|| !Objects.isFloor(this.home[room.getUpperWall() + x][room.getLeftWall() - 1])) {
					x = rand.nextInt(room.getHeight() - 2) + 1;
				}
				this.home[room.getUpperWall() + x][room.getLeftWall()] = Objects.DOOR;
			}
		}
	}

	void setDoors(Room room) {
		Random rand = new Random();

		int x = rand.nextInt(room.getWidth() - 2) + 1; // horizontal
		if (room.getUpperWall() > 0) {
			while (!Objects.isFloor(this.home[room.getUpperWall() + 1][x + room.getLeftWall()])
					|| !Objects.isFloor(this.home[room.getUpperWall() - 1][x + room.getLeftWall()])
					|| !Objects.isFloor(this.home[room.getUpperWall() - 1][x + room.getLeftWall() + 1])
					|| !Objects.isFloor(this.home[room.getUpperWall() + 1][x + room.getLeftWall() + 1])) {
				x = rand.nextInt(room.getWidth() - 2) + 1;
			}
			this.home[room.getUpperWall()][x + room.getLeftWall()] = Objects.DOOR;
			this.home[room.getUpperWall()][x + room.getLeftWall() + 1] = Objects.DOOR;
		}
		x = rand.nextInt(room.getHeight() - 4) + 3; // vertical
		if (room.getLeftWall() > 0) {
			while (!Objects.isFloor(this.home[room.getUpperWall() + x][room.getLeftWall() + 1])
					|| !Objects.isFloor(this.home[room.getUpperWall() + x][room.getLeftWall() - 1])
					|| !Objects.isFloor(this.home[room.getUpperWall() + x + 1][room.getLeftWall() + 1])
					|| !Objects.isFloor(this.home[room.getUpperWall() + x + 1][room.getLeftWall() - 1])) {
				x = rand.nextInt(room.getHeight() - 4) + 3;
			}
			this.home[room.getUpperWall() + x][room.getLeftWall()] = Objects.DOOR;
			this.home[room.getUpperWall() + x + 1][room.getLeftWall()] = Objects.DOOR;
		}

	}

	public void setKitchen(Room room) {
		Table table = new Table(Objects.TABLE, Objects.CHAIR);
		Fridge fridge = new Fridge(Objects.FRIDGE, Objects.FRIDGE_ADD);
		Microwave microwave = new Microwave(Objects.MICROWAVE, Objects.MICROWAVE_ADD);
		Counter counter = new Counter(Objects.COUNTER);
		table.setLocation(room, 10, 10);
		fridge.setLocation(room, 1, 5);
		microwave.setLocation(room, 1, 1);
		counter.setLocation(room, 8, 1);
		interactions.add(fridge);
		nonInteractions.add(table);
		nonInteractions.add(fridge);
		nonInteractions.add(counter);
		nonInteractions.add(microwave);
	}

	public void setSaloon(Room room) {
		Sofa sofa = new Sofa(true, Objects.SOFA, Objects.SOPA_ADD);
		TV tv = new TV(Objects.TV);
		Phone phone = new Phone(Objects.PHONE);
		sofa.setLocation(room, 1, 1);
		tv.setLocation(room, sofa.getWidth() + 2, 3);
		phone.setLocation(room, room.getWidth() - 3, room.getHeight() - 3);
		Decoration tvTable = new Decoration(Objects.TABLE_DECO, tv.getHeight() + 2, tv.getWidth() + 1);
		Decoration phoneTable = new Decoration(Objects.TABLE, 2, 2);
		phoneTable.setLocation(room, room.getWidth() - 3, room.getHeight() - 3);
		tvTable.setLocation(room, sofa.getWidth() + 2, 2);
		nonInteractions.add(sofa);
		nonInteractions.add(tvTable);
		nonInteractions.add(phoneTable);
		interactions.add(tv);
		interactions.add(phone);

	}

	public void setBathroom(Room room) {
		Shower shower = new Shower(Objects.SHOWER, Objects.SHOWER_F);
		shower.setLocation(room, room.getWidth() - 1 - shower.getWidth(), 1);
		Toilet toilet = new Toilet(Objects.TOILET, Objects.TOILET_ADD);
		toilet.setLocation(room, 2, 1);
		Sink sink = new Sink(Objects.SINK);
		sink.setLocation(room, 1, 10);
		paintRoom(room, Objects.BATHROOM1, Objects.BATHROOM2, true);
		interactions.add(shower);
		interactions.add(toilet);
		interactions.add(sink);
	}

	public void setBedroom(Room room) {
		Bed bed1 = new Bed(Objects.BED, Objects.PILLOWS, true);
		bed1.setLocation(room, 5, 1);
		Bed bed2 = new Bed(Objects.BED, Objects.PILLOWS, false);
		bed2.setLocation(room, 5, room.getHeight() - 1 - bed2.getHeight());
		PC pc = new PC(Objects.PC);

		pc.setLocation(room, 1, 8);
		Decoration table = new Decoration(Objects.TABLE_DECO, pc.getHeight() + 2, pc.getWidth() + 1);
		table.setLocation(room, 1, 7);
		nonInteractions.add(table);
		interactions.add(bed1);
		interactions.add(bed2);
		interactions.add(pc);
	}

	public void setGarden(Room room) {
		paintRoom(room, Objects.GARDEN, Objects.GARDEN, true);
		Pool pool = new Pool(Objects.POOL, Objects.POOL_WATER1, Objects.POOL_WATER2);
		pool.setLocation(room, 5, 5);
		interactions.add(pool);
	}

	public void SetupWalls() {
		for (int i = 0; i < MSZ; i++) // outer borders are WALLs
		{
			home[0][i] = Objects.WALL;
			home[i][0] = Objects.WALL;
			home[MSZ - 1][i] = Objects.WALL;
			home[i][MSZ - 1] = Objects.WALL;
		}
	}

	public int getMSZ() {
		return MSZ;
	}

	public int getMinSize() {
		return minSize;
	}

	public Objects[][] getHome() {
		return home;
	}

	public LinkedList<Room> getRooms() {
		return rooms;
	}

	public LinkedList<Room> getSmallRooms() {
		return smallRooms;
	}

	public LinkedList<GeneralItem> getInteractions() {
		return interactions;
	}

	public LinkedList<GeneralItem> getNonInteractions() {
		return nonInteractions;
	}

	public void setNonInteractions(LinkedList<GeneralItem> nonInteractions) {
		this.nonInteractions = nonInteractions;
	}

}

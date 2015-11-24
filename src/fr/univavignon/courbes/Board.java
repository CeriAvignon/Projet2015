package fr.univavignon.courbes;

import java.util.HashMap;
// need a capacity to initialize hashmap
public class Board 
{
	private HashMap<Position, Snake> coreMap;  // liste ! chaque mouvement ajoute un objet dedans
	private HashMap<Position, itemMap> itemMap;
	double sizeX;
	double sizeY; 
			
	Board()
	{
		coreMap = new HashMap<Position, Snake>(50);
		itemMap = new HashMap<Position, itemMap>(50);
		sizeX=boardSizeX();
		sizeY=boardSizeY();
		
	}
	
	double size()
	{
		return coreMap.size();
	}
	
	void addNewElementSnake(Position pos, Snake sna)
	{
		coreMap.put(pos, sna);  // now in the hashmap coreMap, the position pos is taken by the snake sna, collision possible
		
	}
	
	void addNewElementitemMap(Position pos, itemMap ite)
	{
		itemMap.put(pos, ite);
	}
	
	boolean containsPosition(Position pos)
	{
		return coreMap.containsKey(pos);
	}
}
	
	
	
	/*
interface HashMap 
{
    void clear()	// constructor OR new round
    {
        mapSize = 0;
        for (unsigned i = 0; i < maxMapSize; ++i)
        {
            map[i].key = 0;			
            map[i].value = NULL;
        }
    }

    
    T* find(unsigned key)
    {
        if (!key)
            return NULL; // invalid key always returns NULL

        // check every key until we find a match
        unsigned i = hashKey(key) % maxMapSize;
        const unsigned iStart = i;
        do
        {
            if (map[i].key == key)
                return map[i].value;
            i = (i + 1) % maxMapSize;
        }
        while (i != iStart);

        return NULL;
    }

    void insert(unsigned key, T* value)
    {
        ASSERT(key && value && !full() && !find(key));

        unsigned i = hashKey(key) % maxMapSize;
        while (map[i].value)
            i = (i + 1) % maxMapSize;

        map[i].key = key;
        map[i].value = value;
        ++mapSize;
    }

    void erase(unsigned key)
    {
        ASSERT(key && find(key));

        unsigned i = hashKey(key) % maxMapSize;
        while (map[i].key != key)
            i = (i + 1) % maxMapSize;

        map[i].key = 0;
        map[i].value = NULL;
        --mapSize;
    }

    unsigned size()     const { return mapSize; }
    unsigned max_size() const { return maxMapSize; }
    bool empty()        const { return size() == 0; }
    bool full()         const { return size() == max_size(); }

private:

    // rehash the key so we make more efficient use of the map
    unsigned hashKey(unsigned key) { return key*2654435761; }

    struct HashEntry { unsigned key; T* value; };
    HashEntry map[maxMapSize];
    unsigned mapSize;
};
*/

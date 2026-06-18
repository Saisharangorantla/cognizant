
import java.util.HashMap;

public class InventoryManager {
    private HashMap<Integer, Product> inventory = new HashMap<>();

    // O(1)
    public void addProduct(Product product) {
        inventory.put(product.getProductId(), product);
    }

    // O(1)
    public void updateProduct(int id, int qty, double price) {
        Product p = inventory.get(id);
        if (p != null) {
            p.setQuantity(qty);
            p.setPrice(price);
        }
    }

    // O(1)
    public void deleteProduct(int id) {
        inventory.remove(id);
    }

    // O(1)
    public Product searchProduct(int id) {
        return inventory.get(id);
    }

    // O(n)
    public void displayProducts() {
        for (Product p : inventory.values()) {
            System.out.println(p);
        }
    }
}

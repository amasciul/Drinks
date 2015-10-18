package fr.masciulli.drinks.ui;

public interface ItemClickListener<T> {
    void onItemClick(int position, T item);
}

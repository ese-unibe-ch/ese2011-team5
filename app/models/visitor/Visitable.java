/**
 * @author Alt-F4
 */
package models.visitor;

public interface Visitable {
	void accept(Visitor visitor);
}

AddressBook
===========

AddressBook is an address management program. It was the joint creation of David Chapman, Keith Hamm, Jack Ziesing,
John Beck, Noah Hasson, and Wenbo Zhang: The Dragons.

Table of Contents
=================
Introduction
Installation
Opening the program
Using the program
All Contacts Tab
Contact Tab
Import/export
Merge
Quitting the Program

Introduction
============
Address Book was designed by team The Dragons as a simple implementation of an address book for the class CIS 422.

Installation
============
The address book program is designed to run on a machine that has Java 7.  If you don't have Java 7 click here to
download. Once you have Java, you can download the address book program by clicking here.

Opening the program
===================
To open up the application, double click on the .jar file titled AddressBook, and the application will run.
If your system refuses to run it try right clicking and specify Open With > Jar Launcher or some variation.

Using the program
=================
AddressBook automatically opens a blank address book the first time the application is run. In every subsequent
launch, the program will load the contacts that have already been saved into the address book. Your first launch
of the application should look like this.

From here you have the option to select which address book you want to view/edit, as well as the option to merge
a book over to a new book.  Since you just opened it for the first time, you have to create a new address book.
Push the "New" button and you will be prompted to give it a name.  Enter a name of your choice and you will then
be brought to a page like the one below.  You are now in the context of that new address book and sent to the main
page of address books called the all contacts tab.

All Contacts tab
================

The first window that opens is the "All Contacts Tab".  This is the area shown in the picture above and
includes a scroll box with a list of people that are saved in the address book. You are allowed to select one of
the names that appear in the scroll box at a time.  When one is selected, you can then edit or remove that person
from the book.  Edit brings you to the "Contact Tab" and remove deletes the person from the book.  There also is a
new button that allows you to add a new person. This new button redirects you to the "Contact Tab".

New button: brings you to the Contact tab, allowing you to create a new contact.
Edit button: brings you to the Contact tab, bringing the selected clients information into the Contact tabs fields.
Remove button: removes selected contact from the contacts list.

At the top is a sorting tool that allows contacts to be sorted either by last name, or zip code.
Just click the drop down to select either.  Also the area where the contacts are displayed is scrollable.

Contact tab
===========

The Contact Tab is the second area of the program.  This screen is used for editing a contact and adding a new one.
When you have name highlighted in the "All Contacts Tab" and you click edit, you are brought to a screen that looks
similar to the one above, except the content will differ.   Similarly when you click the "New" button from the
"All Contacts Tab" you are then brought to a screen that looks similar to the one above, except all the input fields
will be blank.

Save button: If editing a contact, will overwrite the information that was previously saved in the address book.
             If working on a blank/new contact, will save the information as a new contact in the address book.
Clear button: will erase all the information in all of the fields on the tab.
Cancel button: cancels entry, and doesn't add or change contact.

Import/export
=============

The import/export function allows users to share address books with others.
When a user exports their address book, the contacts are then stored on a .tsv file with a title of your choice
wherever you specify. To access either function, look under the file drop down on the top bar on your screen.

If you choose to export, or hit command E, you will then be asked to give the file a name, and choose a location
for it. like so.

When you press save, the contents of your address book is now saved to the file test.tsv and located on you Desktop.

If you choose to import, or hit command I, you will then be asked to select a .tsv file to import. like this.

When you press okay the contents of test.tsv will now appear in your address book.

Merge
=====

The merge function allows you to take the contacts of two existing address book and merge them into a
new address book with a new name.  The merge is accessed through File > Merge just like import and export,
except from the view of all the address books. Merge requires two address books to be selected.  Once merge is
selected you will be prompted for a new book title.

Steps:
1. File > Merge, or command M with two address books selected.

2. Give the new address book a name

3. Press okay, now you will be brought to your new address book, book3, but the contents of book1 and book2 will be
there.

Print
=====
The print function prints the all the contacts of a given address book.  You can only print when you are viewing
a specific address book.  Select File > Print or command P to trigger the print.  Next you will be directed to the
system print screen where you can choose your printer and other options.

Quitting the program
====================

To quit the program simply click the "x" box in the upper left corner of the window, or click "Main" at the top of
your computer screen and then select "Quit Main".